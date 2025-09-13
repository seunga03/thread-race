package com.multi.cinema;

/*
  =====================================================================
  [동기화 전략(핵심)]
  - 공유 자원: seats 배열 + 각 Seat의 reserved 상태.
  - 임계 구역: reserve() 전체를 synchronized로 감싸서 "중복 예약"을 차단한다.
  - Seat 내부는 동기화하지 않는다(단순 상태 객체로 유지).
  =====================================================================
*/

public class Cinema { // 목적: 좌석 2차원 배열을 초기화하고, 예매 요청을 원자적으로 처리하며, 최종 배치를 렌더링한다.
    // [필드 설계(설명)]

    //- seats: Seat[][] (2차원 배열) * 크기: rows x cols
    //    * 생성자에서 모든 Seat를 A1..A{cols}, B1.., C1.. 형태로 초기화한다.
    Seat[][] seats;

    // - rows: int / cols: int * 좌석 배열의 치수 정보.
    private int rows;
    private int cols;

    // [생성자 동작(설명)]
    public Cinema(int rows, int cols) {
        if (rows <= 0 || cols <= 0) { // rows <= 0 또는 cols <= 0 이면 예외를 던지거나 에러 로그 후 종료.
            throw new IllegalStateException();
        }
        this.rows = rows;
        this.cols = cols;

        this.seats = new Seat[rows][cols]; // seats 배열을 rows x cols 크기로 생성한다.

        for (int i = 0; i < rows; i++) { // 이중 for문으로 (r, c)를 순회하며 Seat를 생성한다.
            // row 문자 계산: (char)('A' + r) → A, B, C ...
            char rowChar = (char) ('A' + i);
            for (int j = 0; j < cols; j++) { // 좌석 ID 문자열: rowChar + (c+1) → "A1", "A2", ...
                String id = rowChar + String.valueOf(j + 1);
                seats[i][j] = new Seat(id); // 생성한 Seat를 seats[r][c]에 할당.
            }
            // 참고: 행/열 수를 보관해 후속 메서드에서 사용한다.
        }
    }

    // [메서드 계약(설명)]

    //- rows(): int / cols(): int * 좌석 배열의 치수 정보를 반환한다.
    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }

    // - reserve(row: int, col: int, customerName: String): boolean
    // * 목적: (row, col) 좌표의 좌석을 예약한다. (메서드 전체를 synchronized로 선언해 "확인 + 갱신"을 원자적으로 보장한다.)
    public synchronized boolean reserve(int row, int col, String customerName) {

        if (customerName.isEmpty() || customerName == null) { // customerName이 비어있지 않은지 확인.
            return false;
        }

        if (0 > row || row >= rows || 0 > col || col >= cols) { // row/col이 유효 범위(0 ≤ row < rows, 0 ≤ col < cols)밖에 있는지 확인한다.
            return false;
        }

        Seat seat = seats[row][col]; // Seat seat = seats[row][col] 참조.
        if (seat.isReserved()) { // seat.isReserved()가 true면 false 반환(이미 예약됨).
            return false;
        }
        // 예약 가능하면 seat.reserve(customerName) 호출 결과(true)를 그대로 반환.
        return seat.reserve(customerName);
    }

    // renderLayout(): String * 목적: 현재 좌석 상태를 사람이 읽기 좋은 문자열로 반환한다.
    public String renderLayout() {
        StringBuilder stringBuilder = new StringBuilder(); // StringBuilder 생성.
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Seat s = seats[r][c];
                stringBuilder.append(s.getId()).append(s.toToken());
                if (c < cols - 1) {
                    stringBuilder.append(' ');
                }
            }
            if (r < rows - 1) {
                stringBuilder.append('\n');
            }

        }
        return stringBuilder.toString();
    }
}
