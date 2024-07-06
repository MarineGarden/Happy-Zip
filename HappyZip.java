package gadget;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
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
	private boolean hasMethods( Class<?> any ) {
		
		Method[] methodsHere = any.getDeclaredMethods();
		if ( methodsHere.length > 0 )
			return true;
		return false;
			
	}
	private static class MockA {}
	private static class MockB {
		
		private void forTest() {}
		
	}
	
	private static class TestableBlock implements Testable {
		
		try {
			
			
			
		} catch ( BrokenGearException e ) {}
		
	}
	private interface Testable {
		
		default boolean test( boolean newTest , boolean oldTests ) throws BrokenGearException {
			
			boolean nextTestsGroup = newTest && oldTests;
			if ( nextTestsGroup )
				return true;
			else
				throw new BrokenGearException( TestableBlock.class );
			
		}
		
	}
	private static class BrokenGearException extends Exception {

		private Class<? extends TestableBlock> testable;
		
		private BrokenGearException( Class<? extends TestableBlock> testable ) {
			
			this.testable = testable;
			
		}

		@Override
		public void printStackTrace( PrintStream stream ) {
			
			stream.append( testable.getName() + "has broken;" );
			super.printStackTrace( stream );
			
		}

	}

}
