package printtime;

public class TestPrintTime {

    public void loadData(){
        System.out.println("start load data");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end load data");
    }
}
