// 실시간으로 비동기 거래를 처리하는 스레드 기반 모듈

class RealTimeTransactionProcessor {
    public void processTransaction(Runnable transactionTask) {
        Thread transactionThread = new Thread(transactionTask);
        transactionThread.start();
    }
}