package gadget;

import java.util.Arrays;

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
	
	public static class Booleans extends NotZeroLength<Boolean> {

		public Booleans( Boolean[] any ) {
			
			super( any );
			
		}

		@Override
		public Boolean[] useDefault() {

			return new Boolean[] { false };
			
		}
		
		public Boolean[] flip( int start , int finish ) {
			
			
			
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
		
	}

}
