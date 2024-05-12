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
	
//	public static class First {
//		
//		public static Boolean read( Boolean[] bits ) {
//			
//			
//			
//		}
//		
//	}
	
//	public static class Same {
//		
//		public static int fromFirst( Boolean[] bits ) {
//			
//			if ( bits.length > 0) {
//				
//				int counter = 0;
//				Boolean first = bits[ 0 ];
//				return -1;
//				
//			}
//			return 0;
//			
//		}
//		
//	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
		//System.out.println( HappyZip.Same.fromFirst( new Boolean[] {} ) == 0 );
	}

}
