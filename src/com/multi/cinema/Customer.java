package com.multi.cinema;


import java.util.Random;

public class Customer extends Thread { // 목적: 고객을 "스레드"로 모델링하여, 동시에 Cinema.reserve()를 호출하고 로그를 남긴다.

    // [필드 설계(설명)]

    // - customerName: String * 로그에 사용할 고객 식별자(예: "고객-03").
    private final String customerName;

    // - cinema: Cinema * 모든 고객이 공유하는 영화관 객체. 이 참조를 통해 reserve() 호출.
    private final Cinema cinema;


    // - random: java.util.Random * 시작 지연과 좌석 선택에 사용(재현성을 원하면 시드 주입).
    private final Random random;

    // - launchSpreadMs: int * 스레드 시작 시 랜덤 대기 최대치(예: 200ms).
    private final int launchSpreadMs;


    // [생성자 동작(설명)]
    public Customer(String customerName, Cinema cinema, Random random, int launchSpreadMs) {
        /*  - 검증:
         * cinema/random이 null이 아닌지 확인.
         * launchSpreadMs가 0 이상인지 확인(음수면 0으로 보정).
         */
        if (cinema == null || random == null) {
            throw new NullPointerException("cinema/random이 null이면 안됩니다.");
        }
        if (launchSpreadMs < 0) {
            throw new IllegalArgumentException("launchSpreadMs가 0 이상이어야 합니다.");
        }
        this.customerName = customerName;
        this.cinema = cinema;
        this.random = random;
        this.launchSpreadMs = launchSpreadMs;
    }


    // [run() 실행 흐름(설명)] 목적: "랜덤 시점에 좌석 하나를 시도"하는 간단한 행동.
    @Override
    public void run() {
        // 1) 시작 지연: 0~launchSpreadMs 사이에서 랜덤 시간만큼 Thread.sleep().
        try {
            Thread.sleep(random.nextInt(launchSpreadMs + 1));
        } catch (InterruptedException e) { // InterruptedException 발생 시: 인터럽트 플래그 복구 후 run() 종료.
            Thread.currentThread().interrupt();
            return;
        }

        // 좌석 좌표 선택
        int rows = cinema.rows();
        int cols = cinema.cols();
        int row = random.nextInt(rows);
        int col = random.nextInt(cols);


        // 예매 시도
        String seatId = (char) ('A' + row) + String.valueOf(col + 1);
        boolean ok = cinema.reserve(row, col, customerName);
        if (ok) {
            System.out.printf("[%s] %s 예매 성공\n", customerName, seatId);
        } else {
            System.out.printf("[%s] %s 이미 예약됨\n", customerName, seatId);
        }
    }
}
