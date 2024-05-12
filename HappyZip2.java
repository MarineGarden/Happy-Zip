package gadget;

import java.util.Arrays;

import gadget.HappyZip.Counter;
import gadget.HappyZip.NotZeroLength;

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
	
	public static class DoubleBooleans extends NotZeroLength<Boolean> {

		public DoubleBooleans( Boolean[] any ) {
			
			super( any );
			//super( new Boolean[ any.length*2 ] )
			
		}

		@Override
		public Boolean[] useDefault() {

			return new Boolean[] { false };
			
		}

		@Override
		public Boolean[] get() {
			
			Boolean[] bits = super.get();
			Boolean[] doubleBits = new Boolean[ bits.length*2 ];
			for ( int i = 0 ; i < bits.length ; i++ )
				doubleBits[ i + bits.length ] = doubleBits[ i ] = bits[ i ];
			return doubleBits;
			
		}
		
		// flip method
		
//		public Boolean[] flip( int start , int finish ) {
//			
//			Boolean[] bits = get();
//			Boolean[] doubleBits = new Boolean[ bits.length*2 ];
//			Counter c = new Counter();
//			while ( c.getCounts() < bits.length ) {
//				
//				int index = c.getCounts();
//				c.check( bits[ index ] );
//				doubleBits[ index ] = ! bits[ index ];
//				doubleBits[ index*2 ] = doubleBits[ index ];
//				
//			}
//			if ( start <= bits.length && start > -1 )
//				if ( finish <= bits.length && finish > -1 )
//					if ( finish >= start )
//						return Arrays.copyOfRange( doubleBits , start , finish );
//					else
//						return Arrays.copyOfRange( doubleBits , start , finish + bits.length );
//			return Arrays.copyOfRange( doubleBits , 0 , doubleBits.length );
//			
//		}
		
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
		
		// Counter test
		Counter c = new Counter();
		while ( c.getCounts() < 3 )
			c.check( true );
		System.out.println( c.getCounts() == 3 );
		c.reset();
		while ( c.getCounts() < 2 )
			c.check( true );
		System.out.println( c.getCounts() == 2 );
		
		// DoubleBooleans test
		System.out.println( Arrays.asList( bits.get() ) );
		DoubleBooleans doubleBits = new DoubleBooleans( bits.get() );
		System.out.println( Arrays.asList( doubleBits.get() ).toString().equals( "[true, true]" ) );
		
	}

}
