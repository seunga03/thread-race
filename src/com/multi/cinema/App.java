package com.multi.cinema;

import java.util.Random;
import java.util.Scanner;

public class App { // 목적: 실행 진입점. Cinema를 만들고 Customer 스레드 N개를 만들어 동시 실행 후 결과를 출력한다.

    // [설정 상수(설명)]
    // - ROWS, COLS: 좌석 배열 크기(예: 3행 4열)
    private static final int ROWS = 3;
    private static final int COLS = 4;

    // - CUSTOMERS: 고객 스레드 수(예: 15)
    private static final int CUSTOMERS = 15;

    // - LAUNCH_SPREAD_MS: 시작 지연 최대치(예: 200)
    private static final int LAUNCH_SPREAD_MS = 200;

    // RANDOM_SEED: 랜덤 시드 (재현 원하면 고정값, 아니면 System.currentTimeMillis())
    private static final long RANDOM_SEED = System.currentTimeMillis();



    public static void main(String[] args) {
        // [main() 실행 흐름(설명)]
        // Cinema 생성
        Cinema cinema = new Cinema(ROWS, COLS);

        // Random 생성 (모든 Customer가 공유)
        Random random = new Random(RANDOM_SEED);

        // Customer 배열 준비
        Customer[] customers = new Customer[CUSTOMERS];

        // Customer 객체 생성 (for문)
        for (int i = 0; i < CUSTOMERS; i++) {
            String name = "고객-" + (i + 1); // 이름: "고객-" + (i+1) (예: 고객-1, 고객-2 …)
            customers[i] =  new Customer(name, cinema, random, LAUNCH_SPREAD_MS);
        }

        // Customer 스레드 시작 (for문)
        for (int i = 0; i < customers.length; i++) {
            customers[i].start();
        }


        // 모든 Customer 종료 대기 (for문)
        for (int i = 0; i < customers.length; i++) {
            try {
                customers[i].join(); // 모든 스레드가 끝날 때까지 App.main()은 기다림
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("메인 스레드 인터럽트됨");
                return;
            }
        }

        // 최종 결과 출력
        System.out.println("=== 최종 좌석 배치 ===");
        System.out.println(cinema.renderLayout());

    }
}

/*
  =========================
  [동기화 실험 방법]
  - Cinema.reserve()에 synchronized 키워드를 제거하면:
    → 여러 고객이 동시에 같은 좌석 예약 성공하는 경우 발생
  - 다시 synchronized를 붙이면:
    → 동시에 들어와도 한 명만 성공, 중복 예매 0건으로 안정화
  =========================
  [주의]
  - 배열은 고정 크기이므로 CUSTOMERS 상수와 일치해야 함.
  - join()은 꼭 호출해야 "모든 고객 스레드 종료 후 최종 좌석 배치 출력"이 보장된다.
*/
