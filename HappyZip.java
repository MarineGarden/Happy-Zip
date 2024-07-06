package gadget;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.PrintStream;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class HappyZip extends JFrame implements MouseMotionListener {
	
	public static void main( String[] args ) {

		HappyZip test = new HappyZip();
		System.out.println( test.testForHasMethodAnnotations() );
	}
	
	@Override
	public void mouseDragged( MouseEvent event ) {}

	@Override
	public void mouseMoved( MouseEvent event ) {}

	private boolean testForHasMethodAnnotations() {
		
		return ( ! hasMethodAnnotations( MockC.class ) ) && ( hasMethodAnnotations( MockB.class ) );
		
	}
	private boolean hasMethodAnnotations( Class<?> any ) {
		
		if ( hasMethods( any ) )
			for ( Method method : any.getDeclaredMethods() )
				if ( method.getDeclaredAnnotations().length > 0 )
					return true;
		return false;
		
	}
	
	private boolean testForIsAnnotatedMethod() {
		
		try {
			
			boolean AmethodTest = isAnnotatedMethod( MockB.class.getDeclaredMethod( "forTestA" ) );
			boolean BmethodTest = ! isAnnotatedMethod( MockB.class.getDeclaredMethod( "forTestB" ) );
			return AmethodTest && BmethodTest;
			
		} catch ( NoSuchMethodException | SecurityException e ) { e.printStackTrace(); }
		
		return false;
		
	}
	private boolean isAnnotatedMethod( Method any ) {
		
		return any.getAnnotations().length > 0;
		
	}
	
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
		
		@Tested( testClass = "" , expected = "" )
		private void forTestA() {}
		
		private void forTestB() {}
		
	}
	private static class MockC {
		
		private void forTestA() {}
		
	}
	
	private static class TestableBlock implements Testable {
		
		protected boolean oldTests = true;
		
		private TestableBlock( boolean newTest ) {
			
			try {
				
				oldTests = test( newTest , oldTests);
				
			} catch ( BrokenGearException e ) {
				
				e.printStackTrace( System.out );
				
			}
			
		}
		
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
	
	@Retention( RUNTIME )
	@Target({ FIELD , METHOD , CONSTRUCTOR })
	private @interface Tested {

		String testClass();
		String expected();
		
	}

}
