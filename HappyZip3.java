package gadget;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

import gadget.HappyZip2.NotZeroLength;

@SuppressWarnings("serial")
public class HappyZip3 {
	
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
			
			return p.y > p.x;
			
		}
		
		public boolean isBottomTriangle( Point p ) {
			
			return p.x > p.y;
			
		}
		
	}
	
	public static class Flip extends NotZeroLength<Boolean> {
		
		public Flip( Boolean[] bits ) {
			
			super( bits );
			
		}

		@Override
		public Boolean[] useDefault() {

			return new Boolean[] { false };
			
		}
		
		public Boolean[] doTask( int start , int finish ) {
			
			YinYangRect boundaries = new 
			
		}
		
	}
	
//	public Boolean[] flip( int start , int finish ) {
//		
//		Boolean[] bits = get();
//		Boolean[] doubleBits = new Boolean[ bits.length*2 ];
//		Counter c = new Counter();
//		while ( c.getCounts() < bits.length ) {
//			
//			int index = c.getCounts();
//			c.check( bits[ index ] );
//			doubleBits[ index ] = ! bits[ index ];
//			doubleBits[ index*2 ] = doubleBits[ index ];
//			
//		}
//		if ( start <= bits.length && start > -1 )
//			if ( finish <= bits.length && finish > -1 )
//				if ( finish >= start )
//					return Arrays.copyOfRange( doubleBits , start , finish );
//				else
//					return Arrays.copyOfRange( doubleBits , start , finish + bits.length );
//		return Arrays.copyOfRange( doubleBits , 0 , doubleBits.length );
//		
//	}

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
		System.out.println( rect.isOutside( outsideTop ) );
		System.out.println( rect.isOutside( outsideRight ) );
		System.out.println( rect.isOutside( outsideBottom ) );
		System.out.println( rect.isOutside( outsideLeft ) );
		System.out.println( rect.isUpperTriangle( insideUpperPart ) );
		System.out.println( rect.isBottomTriangle( insideBottomPart ) );
	}

}
