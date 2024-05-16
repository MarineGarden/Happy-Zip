package gadget;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;

@SuppressWarnings("serial")
public class HappyZip4 {
	
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
		
		protected Boolean[] flip() {
			
			Boolean[] result = get();
			for ( int i = 0 ; i < result.length ; i++ )
				result[ i ] = ! result[ i ];
			return result;
			
		}
		
		protected DoubleBooleans getThis() {
			
			return this;
			
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
				//doubleBits.getThis().getClass().getSuperclass().get
				
	}

}
