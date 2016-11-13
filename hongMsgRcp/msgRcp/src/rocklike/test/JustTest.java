package rocklike.test;

public class JustTest {

	public static void main(String[] args) {
		JustTest main = new JustTest();
		main.randomTest();
	}

	void randomTest(){
		for(int i=0; i<100; i++){
			double r = Math.random();
			System.out.printf("%s  :: %s\n", r, (int)(r*50));

		}
	}
}
