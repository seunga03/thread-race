# 🎬 Java Thread Practice — 영화관 좌석 예매 스레드 시뮬레이터

> 자유 주제로 구현한 **자바 스레드 + synchronized 기초 실습**
> 상세 회고 및 몰입 경험은 [👉 블로그 글 보기](https://co-din9.tistory.com/81)

---

## 📌 프로젝트 개요

* 주제: 영화관 좌석 예매 시스템을 스레드로 시뮬레이션
* 학습 목표: **Race Condition, 임계구역, synchronized 키워드 체감하기**
* 구조: App → Customer(Thread) → Cinema(관리자) → Seat(엔티티)

---

## 🗂️ 코드 구조

```
src/main/java/com/multi/cinema/
 ├─ App.java        // 실행 진입점
 ├─ Customer.java   // 고객 스레드
 ├─ Cinema.java     // 좌석 관리 및 동기화
 └─ Seat.java       // 좌석 엔티티
```

---

## 🔎 클래스별 분석

### 1. Seat

* 좌석 상태 관리 (id, reserved, reservedBy)
* `reserve(customer)` → 성공 시 true, 실패 시 false
* `toToken()` → `[O]` / `[ ]` 출력

### 2. Cinema

* 좌석 2차원 배열 관리
* `synchronized reserve(row,col,customer)`
  → 임계구역 보호, Race Condition 방지
* `renderLayout()` → 좌석 배치 문자열 출력

### 3. Customer

* 스레드로 동작
* run()

  1. 랜덤 지연
  2. 랜덤 좌석 선호
  3. `cinema.reserve()` 호출
  4. 성공/실패 로그 출력

### 4. App

* 실행 진입점
* 실행 흐름

  1. Cinema 생성
  2. Customer\[] 배열 생성
  3. start() → join()
  4. 최종 좌석 배치 출력

---

## 📚 학습 목표 개념 정리

### 🔀 Race Condition (경합 조건)

* **정의**: 여러 스레드가 동시에 같은 자원에 접근 → 실행 순서에 따라 결과 달라짐
* **예시**: 두 고객이 동시에 A1 좌석 예매 → 둘 다 성공 처리될 수 있음
* **의의**: 멀티스레드 환경의 대표적 오류, 결과가 비결정적(non-deterministic)

---

### 🚧 Critical Section (임계구역)

* **정의**: 공유 자원 접근 시 동기화가 필요한 코드 블록
* **특징**: 반드시 원자적(atomic)으로 실행되어야 함
* **이 프로젝트의 임계구역**: `Cinema.reserve()` 전체

---

### 🔒 synchronized (자바 키워드)

* **정의**: 자바에서 임계구역을 보호하는 키워드
* **동작**: 락(lock)을 걸어 한 번에 하나의 스레드만 접근 허용
* **장점**: Race Condition 방지, 일관성 유지
* **단점**: 성능 저하 가능, 락 경합 발생

---

### 💡 체감한 점

* synchronized 제거 → 중복 예매가 발생한다.
* synchronized 적용 → 중복 예약이 없음, 안정적으로 동작한다.
* **결론**: 로그와 최종 좌석 배치로 "동기화 전/후 차이"를 바로 확인할 수 있다.

---

## 🧪 실행 예시

```text
[고객-9] C4 예매 성공
[고객-4] B1 예매 성공
[고객-7] A4 예매 성공
...
=== 최종 좌석 배치 ===
A1[O] A2[ ] A3[O] A4[O]
B1[O] B2[ ] B3[ ] B4[O]
C1[ ] C2[ ] C3[O] C4[O]
```

---

## 🗺️ 클래스 다이어그램

<img width="3688" height="1600" alt="image" src="https://github.com/user-attachments/assets/9ce54ec6-f533-4189-af93-a67b13ff9917" />


---

## 📊 시퀀스 다이어그램 (예매 시나리오)

<img width="3128" height="2324" alt="image" src="https://github.com/user-attachments/assets/f726e686-7af6-4772-bfb3-2db80c836dd3" />


---

## 📖 더 자세한 회고

설계 접근법, 버그 분석 과정, 몰입 경험은 블로그에 정리했습니다.
👉 [블로그 글 보러가기](https://co-din9.tistory.com/81)
