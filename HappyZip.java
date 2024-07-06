package gadget;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Method;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class HappyZip extends JFrame implements MouseMotionListener {
	
	public static void main( String[] args ) {

		new HappyZip();
		
	}
	
	@Override
	public void mouseDragged( MouseEvent event ) {}

	@Override
	public void mouseMoved( MouseEvent event ) {}

	private boolean testForHasMethods() {
		
		return ( ! hasMethods( MockA.class ) ) && ( hasMethods( MockB.class ) );
		
	}
	private static class MockA {}
	private static class MockB {
		
		private void forTest() {}
		
	}
	private boolean hasMethods( Class<?> any ) {
		
		Method[] methodsHere = any.getDeclaredMethods();
		if ( methodsHere.length > 0 )
			return true;
		return false;
			
	}
	
	private static class TestBlock implements Testable {
		
		
		
	}
	private interface Testable {
		
		default boolean test( boolean newTest , boolean oldTests ) {
			
			return newTest && oldTests;
			
		}
		
	}

}
