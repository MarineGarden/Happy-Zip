package gadget;

import java.util.Arrays;

public class HappyZip {
	
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
	
	public static class First {
		
		public static Boolean read( NotZeroLength<Boolean> bits ) {
			
			return bits.get()[ 0 ];
			
		}
		
		public static Boolean[] write( NotZeroLength<Boolean> bits , Boolean first ) {
			
			Boolean[] result = bits.get();
			result[ 0 ] = first;
			return result;
			
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
	
	public static class Same extends Counter {
		
		public int fromFirst( NotZeroLength<Boolean> bits ) {
			
			reset(); // safety first
			for ( Boolean bit : bits.get() )
				if ( ! check( First.read( bits ) == bit ) )
					break;
			return getCounts();
			
		}
		
	}
	
	public static class One extends Same {
		
//		public Boolean[] truely( NotZeroLength<Boolean> bits ) {
//			
//			boolean oneBitOnly = fromFirst( bits ) == 1;
//			if ( ! oneBitOnly )
//			
//		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

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
				
				// First test
				bits = new NotZeroLength<Boolean>( new Boolean[] { true , false } ) {
					
					@Override
					public Boolean[] useDefault() {

						return new Boolean[] { false };
						
					}
					
				};
				System.out.println( First.read( bits ) );
				First.write( bits , false );
				System.out.println( ! First.read( bits ) );
				
				// Counter test
				Counter c = new Counter();
				while ( c.getCounts() < 3 )
					c.check( true );
				System.out.println( c.getCounts() == 3 );
				c.reset();
				while ( c.getCounts() < 2 )
					c.check( true );
				System.out.println( c.getCounts() == 2 );
				
				// Same test
				System.out.println( Arrays.asList( bits.get() ) );
				Same s = new Same();
				System.out.println( s.fromFirst( bits ) == 2 );
				First.write( bits , true );
				System.out.println( s.fromFirst( bits ) == 1 );
	}

}
