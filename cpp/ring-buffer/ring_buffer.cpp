#include <atomic>
#include <chrono>
#include <cstddef>
#include <cstdint>
#include <iomanip>
#include <iostream>
#include <mutex>
#include <queue>
#include <thread>
#include <vector>

class RingBuffer {
public:
  explicit RingBuffer(size_t size) : buffer_(size) {}

  // Returns true on success. Fails if the buffer is full.
  bool push(int item) {
    uint64_t write_idx = write_idx_.load(std::memory_order_relaxed);
    if (write_idx - cached_read_idx_ == buffer_.size()) {
      cached_read_idx_ = read_idx_.load(std::memory_order_acquire);
      if (write_idx - cached_read_idx_ == buffer_.size()) {
        return false;
      }
    }
    buffer_[write_idx & (buffer_.size() - 1)] = item;
    write_idx_.store(write_idx + 1, std::memory_order_release);
    return true;
  }

  // Returns true on success. Fails if the buffer is empty.
  bool pop(int *dest) {
    uint64_t read_idx = read_idx_.load(std::memory_order_relaxed);
    if (cached_write_idx_ == read_idx) {
      cached_write_idx_ = write_idx_.load(std::memory_order_acquire);
      if (cached_write_idx_ == read_idx) {
        return false;
      }
    }
    *dest = buffer_[read_idx & (buffer_.size() - 1)];
    read_idx_.store(read_idx + 1, std::memory_order_release);
    return true;
  }

private:
  std::vector<int> buffer_;
  alignas(64) std::atomic<uint64_t> read_idx_{0};
  alignas(64) uint64_t cached_read_idx_{0};
  alignas(64) std::atomic<uint64_t> write_idx_{0};
  alignas(64) uint64_t cached_write_idx_{0};
};

constexpr uint64_t bmtCount = 500000;

template <typename RingBufferType> double benchmark(RingBufferType &rb) {
  auto start = std::chrono::system_clock::now();
  std::thread workers[2] = {
      std::thread([&]() {
        for (uint64_t i = 0; i < bmtCount; ++i) {
          int count = 1000;
          while (0 < count) {
            if (rb.push(count)) {
              count--;
            }
          }
        }
      }),
      std::thread([&]() {
        int result;
        for (uint64_t i = 0; i < bmtCount; ++i) {
          int count = 1000;
          while (0 < count) {
            if (rb.pop(&result)) {
              count--;
            }
          }
        }
      })};
  for (auto &w : workers) {
    w.join();
  }
  auto end = std::chrono::system_clock::now();
  double duration = std::chrono::duration_cast<std::chrono::nanoseconds>(end - start).count();
  const int count = bmtCount * (1000 + 1000);
  std::cerr << count << " ops in " << duration << " ns \t";
  return 1000000.0 * bmtCount * (1000 + 1000) / duration;
}

int main() {
  RingBuffer rb(2 * 1024 * 1024);
  std::cout << "RingBuffer: " << benchmark(rb) << " ops/ms\n";
};
