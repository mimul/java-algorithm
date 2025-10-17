# 컴파일러 설정
CXX = g++
CXXFLAGS = -Wall -O3 -march=native -std=c++17

# 타겟 이름과 소스 파일
TARGET = ring_buffer
SRC = ring_buffer.cpp

# 기본 빌드 규칙
all: $(TARGET)

$(TARGET): $(SRC)
	$(CXX) $(CXXFLAGS) -o $(TARGET) $(SRC)

# 실행 파일 및 중간 파일 정리
clean:
	rm -f $(TARGET)
