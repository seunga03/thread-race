package com.multi.cinema;

public class Seat { // 목적: 단일 좌석(예: A3)의 상태를 보관하고, 예약 가능 여부를 판정한다.
    /*
      [주의/권장]
      - Seat는 가능한 "상태만 가지는 단순 객체"로 유지한다.
      - 동시성 제어는 상위 레벨(Cinema)에서 일괄 처리한다.
    */


    // [필드 설계(설명)]
    // id: 좌석 식별자 문자열 (예: "A3")     * 생성자에서 주입됨. 변경 불가로 두면 안전함.
    private final String id;

    // reserved: 예약 여부(boolean)     * 기본값 false. 예약 성공 시 true.
    private boolean reserved;

    // reservedBy: 예약자 이름(String)     * 로그/추적용. 기본 null 또는 빈 문자열.
    private String reservedBy;


    // [생성자 동작(설명)]
    public Seat(String id) { // - 입력: 좌석 id 문자열
        // - 예외/검증(선택): id가 비어있으면 예외를 던지거나 로그 후 기본값 처리.
        if (id == null) {
            throw new NullPointerException("id는 필수값입니다.");
        }

        // - 내부: id를 저장하고, reserved=false, reservedBy=null로 초기화한다.
        this.id = id;
        this.reserved = false;
        this.reservedBy = null;

    }

    // [메서드 계약(설명)]
    // - getId(): String * 좌석 ID를 그대로 반환한다.
    public String getId() {
        return id;
    }

    // - isReserved(): boolean * 현재 좌석의 예약 상태를 반환한다.
    public boolean isReserved() {
        return reserved;
    }

    // - reserve(customerName: String): boolean * 목적: 좌석을 예약한다.
    public boolean reserve(String customerName) {
        if (customerName == null | customerName.isEmpty()) { // 전제: customerName은 null/빈 문자열이 아니어야 한다(검증은 선택).
            throw new IllegalArgumentException("customerName은 null/빈 문자열 불가");
        }
        if (reserved) { // 1) reserved가 이미 true면 false 반환(예약 실패).
            return false;
        } else { // 2) reserved=false라면 reserved=true, reservedBy=customerName로 세팅 후 true 반환(예약 성공).
            reserved = true;
            reservedBy = customerName;
            return true;
        }
    }

    // - toToken(): String * 목적: 레이아웃 표시용 토큰 반환.
    public String toToken() {
        if (reserved) { // reserved==true → "[O]"
            return "[O]";
        } else { // reserved==false → "[ ]"
            return "[ ]";
        }
    }
}
