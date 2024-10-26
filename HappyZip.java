package felix.lin.gadget;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

@SuppressWarnings("serial")
public class HappyZip {
	
	public @interface TestGadgetMax {}
	
	public @interface TestGadgetA {}
	
	public @interface TestGadgetAA {}
	
	public @interface TestArgument {}
	
	@Retention( SOURCE )
	@Target( TYPE )
	public @interface TestGadget {}
	
	@TestGadget
	public static class RunnableClass implements Runnable {

		private final Class<?> original;
		
		public RunnableClass( Class<?> original ) {
			
			this.original = original;
			
		}
		
		public Class<?> getOriginalClass() {
			
			return original;
			
		}
		
		@Override
		public void run() {}
		
	}
	
	@TestGadget
	public interface Testable {
		
		default boolean test( Class<?> target ) {
			
			boolean isAllPassed = false;
			Field[] testedFields = target.getDeclaredFields();
			for ( Field f : testedFields )
				if ( hasTests( f ) )
					if ( pass( f , new HappyZip() ) )
						isAllPassed = true;
					else {
					
						isAllPassed = false;
						break;
					
					}
			return isAllPassed;
			
		};
		
		default boolean hasTests( Field f ) {
			
			return f.getAnnotationsByType( Test.class ).length > 0;
			
		}
		
		default boolean pass( Field field , Object fieldOwner ) {
			
			for ( Test test : field.getDeclaredAnnotationsByType( Test.class ) )
				try {
					
					if ( test.actual().equals( "" ) )
						if ( ! test.expected().equals( field.get( fieldOwner ).toString() ) )
							return false;
					
				} catch ( IllegalArgumentException | IllegalAccessException e ) {	 e.printStackTrace(); }
			return true;
			
		};
		
	}
	
	@TestGadget
	public static class RunnableClassTest extends RunnableClass implements Testable {

		private boolean isAllPassed = false;
		
		public RunnableClassTest( Class<?> original ) {
			super( original );
			
			run();
			
		}

		@Override
		public void run() {

			isAllPassed = test( getOriginalClass() );
			
		}
		
		public boolean isAllPassed() {
			
			return isAllPassed;
			
		}
		
		public static boolean run( Class<?> target ) {
			
			return new RunnableClassTest( target ).isAllPassed();
			
		}
		
	}
	
	@TestGadget
	@Retention( RUNTIME )
	@Target( FIELD )
	@Repeatable( Tests.class )
	public @interface Test {
		
		boolean MAIN = RunnableClassTest.run( HappyZip.class );
		
		String name();
		String expected();
		String actual();

	}
	
	@TestGadget
	@Retention( RUNTIME )
	@Target( FIELD )
	public @interface Tests {
		
		Test[] value();
		
	}
	
	@Test( name = "no-operation TestGadget test" , expected = "ok" , actual = "" )
	private Object noOperationTestForTestGadget = "ok";
	
	public static class TestArgumentFormat {
		
		private String[] operationQueue;
		private Object testResult;
		
		public TestArgumentFormat( String operations ) {
			
			operationQueue = operations.split( ">" );
			
		}
		
		// @Test( expected = "ok" , actual = "a(12)>b" )
		// forEach()
		
		public TestArgumentFormat next() {
			
			if ( operationQueue.length > 0 )
				if ( isMethod( operationQueue[ 0 ] ) ) {
					
					
					
				}
			return this;
			
		}
		
		private boolean isMethod( String operation ) {
			
			return operation.contains( "(" ) && operation.contains( ")" );
			
		}
		
		public Object getTestResult() {
			
			return testResult;
			
		}
		
	}
	
	@TestGadget
	public interface Multitestable extends Testable {
		
		@Override
		default boolean pass( Field field , Object fieldOwner ) {
			
			for ( Test test : field.getDeclaredAnnotationsByType( Test.class ) )
				try {
					
					if ( ! test.actual().equals( "" ) )
						if ( ! test.expected().equals( "" ) )
							return false;
					
				} catch ( IllegalArgumentException e ) {	 e.printStackTrace(); }
			return true;
			
		}
		
	}
	
	public static abstract class NotZeroLength<T> {
		
		private T[] any;
		
		public NotZeroLength( T[] any ) {
			
			if ( any.length == 0 )
				this.any = useDefault();
			else
				this.any = any;
			
		}
		
		public abstract T[] useDefault();
		
		public T[] get() {
			
			return any;
			
		}
		
		public T[] fill( T[] another ) {
			
			for ( int i = 0 ; i < another.length ; i++ )
				another[ i ] = any[ i % any.length ];
			return another;
			
		}
		
	}

	public static class DoubleBooleans extends NotZeroLength<Boolean> {

		public DoubleBooleans( Boolean[] any ) {
		
			super( any );
		
		}
		
		@Override
		public Boolean[] useDefault() {

			return new Boolean[] { false };
		
		}

		@Override
		public Boolean[] get() {
		
			return fill( new Boolean[ super.get().length*2 ] );
		
		}
		
		public Boolean[] flip() {
			
			Boolean[] result = get();
			for ( int i = 0 ; i < result.length ; i++ )
				result[ i ] = ! result[ i ];
			return result;
			
		}
		
	}
	
	public static class YinYangRect extends Rectangle {
		
		//  y                          slope : x = y
		//  
		//  oooooooooo
		//  o               o  o
		//  o           o      o
		//  o       o          o
		//  o   o              o
		//  oooooooooo   x
		
		public YinYangRect( int minX , int maxX , int minY , int maxY ) {
			
			super( minX , minY , maxX - minX , maxY - minY );
			
		}
		
		public boolean isOutside( Point p ) {
			
			return ( p.x < x || p.x > x + width ) || ( p.y < y || p.y > y + height );
			
		}
		
		public boolean isUpperTriangle( Point p ) {
			
			return p.y > p.x && p.x > x && p.y < y + height;
			
		}
		
		public boolean isBottomTriangle( Point p ) {
			
			return p.x > p.y && p.y > y && p.x < x + width;
			
		}
		
		public enum YinYangLocation {
			
			OUTSIDE, UPPER_TRIANGLE, BOTTOM_TRIANGLE, BOUNDARIES
			
		}
		
		public YinYangLocation where( Point p ) {
			
			if ( isOutside( p ) )
				return YinYangLocation.OUTSIDE;
			if ( isUpperTriangle( p ) )
				return YinYangLocation.UPPER_TRIANGLE;
			if ( isBottomTriangle( p ) )
				return YinYangLocation.BOTTOM_TRIANGLE;
			
			return YinYangLocation.BOUNDARIES;
			
		}
		
	}
	
	public static class Flip extends DoubleBooleans {
		
		private YinYangRect boundaries;
		
		public Flip( Boolean[] bits ) {
			
			super( bits );
			boundaries = new YinYangRect( 0 , bits.length , 0 , bits.length );
			
		}
		
		public YinYangRect.YinYangLocation classify( int start , int finish ) {
			
			return boundaries.where( new Point( start , finish ) );
			
		}
		
		public Boolean[] doTask( int start , int finish ) {
			
			switch ( classify( start , finish ) ) {
			
				case OUTSIDE: return Arrays.copyOfRange( flip() , 0 , boundaries.width );
				case UPPER_TRIANGLE: return Arrays.copyOfRange( flip() , start , finish );
				case BOTTOM_TRIANGLE: return Arrays.copyOfRange( flip() , start , finish + boundaries.width );
				case BOUNDARIES: return Arrays.copyOfRange( flip() , start , finish );
				default: return useDefault();
			
			}
			
		}
		
	}

	public static void main(String[] args) {

		System.out.println( Test.MAIN );
		
//				// NotZeroLength test
//				NotZeroLength<Boolean> bits = new NotZeroLength<Boolean>( new Boolean[] {} ) {
//
//					@Override
//					public Boolean[] useDefault() {
//
//						return new Boolean[] { false };
//								
//					}
//							
//				};
//				System.out.println( Arrays.asList( bits.get() ).toString().equals( "[false]" ) );
//				bits = new NotZeroLength<Boolean>( new Boolean[] { true } ) {
//							
//					@Override
//					public Boolean[] useDefault() {
//
//						return new Boolean[] { false };
//								
//					}
//							
//				};
//				System.out.println( Arrays.asList( bits.get() ).toString().equals( "[true]" ) );
//				bits = new NotZeroLength<Boolean>( new Boolean[] { true , false } ) {
//					
//					@Override
//					public Boolean[] useDefault() {
//
//						return new Boolean[] { false };
//								
//					}
//							
//				};
//				System.out.println( Arrays.asList( bits.fill( new Boolean[ 5 ] ) ).toString().equals( "[true, false, true, false, true]" ) );
//				
//				// DoubleBooleans test
//				DoubleBooleans doubleBits = new DoubleBooleans( bits.get() );
//				System.out.println( Arrays.asList( doubleBits.get() ).toString().equals( "[true, false, true, false]" ) );
//				System.out.println( Arrays.asList( doubleBits.flip() ).toString().equals( "[false, true, false, true]" ) );
//				
//				// YinYangRect test
//				YinYangRect rect = new YinYangRect( 0 , 3 , 0 , 3 );
//				Point outsideLeft = new Point( -1 , 1 );
//				Point outsideRight = new Point( 4 , 1 );
//				Point outsideTop = new Point( 1 , 4 );
//				Point outsideBottom = new Point( 1 , -1 );
//				Point insideUpperPart = new Point( 1 , 2 );
//				Point insideBottomPart = new Point( 2 , 1 );
//				Point onTheBoundary = new Point( 2 , 2 );
//				System.out.println( rect.isOutside( outsideTop ) );
//				System.out.println( rect.isOutside( outsideRight ) );
//				System.out.println( rect.isOutside( outsideBottom ) );
//				System.out.println( rect.isOutside( outsideLeft ) );
//				System.out.println( rect.isUpperTriangle( insideUpperPart ) );
//				System.out.println( rect.isBottomTriangle( insideBottomPart ) );		
//				System.out.println( rect.where( outsideTop ).name().equals( "OUTSIDE" ) );
//				System.out.println( rect.where( outsideRight ).name().equals( "OUTSIDE" ) );
//				System.out.println( rect.where( outsideBottom ).name().equals( "OUTSIDE" ) );
//				System.out.println( rect.where( outsideLeft ).name().equals( "OUTSIDE" ) );
//				System.out.println( rect.where( insideUpperPart ).name().equals( "UPPER_TRIANGLE" ) );
//				System.out.println( rect.where( insideBottomPart ).name().equals( "BOTTOM_TRIANGLE" ) );
//				System.out.println( rect.where( onTheBoundary ).name().equals( "BOUNDARIES" ) );
//				
//				// Flip test
//				Flip f = new Flip( new Boolean[] { true , true , false , true , true } );
//				System.out.println( f.classify( 1 , 3 ).name() + " : " + Arrays.asList( f.doTask( 1 , 3 ) ).toString().equals( "[false, true]" ) );
//				System.out.println( f.classify( 3 , 1 ).name() + " : " + Arrays.asList( f.doTask( 3 , 1 ) ).toString().equals( "[false, false, false]" ) );
//				System.out.println( f.classify( 1 , 5 ).name() + " : " + Arrays.asList( f.doTask( 1 , 5 ) ).toString().equals( "[false, true, false, false]" ) );
//				System.out.println( f.classify( 1 , 6 ).name() + " : " + Arrays.asList( f.doTask( 1 , 6 ) ).toString().equals( "[false, false, true, false, false]" ) );
	}

}
