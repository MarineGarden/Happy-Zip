package gadget;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

@SuppressWarnings("serial")
public class HappyZip2 {
	
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
		
		private Boolean[] copyTheFlip( int start , int finish ) {
			
			return Arrays.copyOfRange( flip() , start , finish );
			
		}
		
		public Boolean[] doTask( int start , int finish ) {
			
			switch ( classify( start , finish ) ) {
			
				case OUTSIDE: return copyTheFlip( 0 , boundaries.width );
				case UPPER_TRIANGLE: return copyTheFlip( start , finish );
				case BOTTOM_TRIANGLE: return copyTheFlip( start , finish + boundaries.width );
				case BOUNDARIES: return copyTheFlip( start , finish );
				default: return useDefault();
			
			}
			
		}
		
	}
	
	public static class Counter {
		
		private int value = 0;
		
		public boolean check( boolean suitable ) {
			
			if ( suitable )
				value++;
			return suitable;
			
		}
		
		public int getCounts() {
			
			return value;
			
		}
		
		public void reset() {
			
			value = 0;
			
		}
		
	}

	public static void main(String[] args) {
		// TODO

				// NotZeroLength test
				NotZeroLength<Boolean> bits = new NotZeroLength<Boolean>( new Boolean[] {} ) {

					@Override
					public Boolean[] useDefault() {

						return new Boolean[] { false };
								
					}
							
				};
				System.out.println( Arrays.asList( bits.get() ).toString().equals( "[false]" ) );
				bits = new NotZeroLength<Boolean>( new Boolean[] { true } ) {
							
					@Override
					public Boolean[] useDefault() {

						return new Boolean[] { false };
								
					}
							
				};
				System.out.println( Arrays.asList( bits.get() ).toString().equals( "[true]" ) );
				bits = new NotZeroLength<Boolean>( new Boolean[] { true , false } ) {
					
					@Override
					public Boolean[] useDefault() {

						return new Boolean[] { false };
								
					}
							
				};
				System.out.println( Arrays.asList( bits.fill( new Boolean[ 5 ] ) ).toString().equals( "[true, false, true, false, true]" ) );
				
				// DoubleBooleans test
				DoubleBooleans doubleBits = new DoubleBooleans( bits.get() );
				System.out.println( Arrays.asList( doubleBits.get() ).toString().equals( "[true, false, true, false]" ) );
				System.out.println( Arrays.asList( doubleBits.flip() ).toString().equals( "[false, true, false, true]" ) );
				
				// YinYangRect test
				YinYangRect rect = new YinYangRect( 0 , 3 , 0 , 3 );
				Point outsideLeft = new Point( -1 , 1 );
				Point outsideRight = new Point( 4 , 1 );
				Point outsideTop = new Point( 1 , 4 );
				Point outsideBottom = new Point( 1 , -1 );
				Point insideUpperPart = new Point( 1 , 2 );
				Point insideBottomPart = new Point( 2 , 1 );
				Point onTheBoundary = new Point( 2 , 2 );
				System.out.println( rect.isOutside( outsideTop ) );
				System.out.println( rect.isOutside( outsideRight ) );
				System.out.println( rect.isOutside( outsideBottom ) );
				System.out.println( rect.isOutside( outsideLeft ) );
				System.out.println( rect.isUpperTriangle( insideUpperPart ) );
				System.out.println( rect.isBottomTriangle( insideBottomPart ) );		
				System.out.println( rect.where( outsideTop ).name().equals( "OUTSIDE" ) );
				System.out.println( rect.where( outsideRight ).name().equals( "OUTSIDE" ) );
				System.out.println( rect.where( outsideBottom ).name().equals( "OUTSIDE" ) );
				System.out.println( rect.where( outsideLeft ).name().equals( "OUTSIDE" ) );
				System.out.println( rect.where( insideUpperPart ).name().equals( "UPPER_TRIANGLE" ) );
				System.out.println( rect.where( insideBottomPart ).name().equals( "BOTTOM_TRIANGLE" ) );
				System.out.println( rect.where( onTheBoundary ).name().equals( "BOUNDARIES" ) );
				
				// Flip test
				Flip f = new Flip( new Boolean[] { true , true , false , true , true } );
				System.out.println( f.classify( 1 , 3 ).name() + " : " + Arrays.asList( f.doTask( 1 , 3 ) ).toString().equals( "[false, true]" ) );
				System.out.println( f.classify( 3 , 1 ).name() + " : " + Arrays.asList( f.doTask( 3 , 1 ) ).toString().equals( "[false, false, false]" ) );
				System.out.println( f.classify( 1 , 5 ).name() + " : " + Arrays.asList( f.doTask( 1 , 5 ) ).toString().equals( "[false, true, false, false]" ) );
				System.out.println( f.classify( 1 , 6 ).name() + " : " + Arrays.asList( f.doTask( 1 , 6 ) ).toString().equals( "[false, false, true, false, false]" ) );
				
				// Counter test
				Counter c = new Counter();
				while ( c.getCounts() < 3 )
					c.check( true );
				System.out.println( c.getCounts() == 3 );
				c.reset();
				while ( c.getCounts() < 2 )
					c.check( true );
				System.out.println( c.getCounts() == 2 );
	}

}
