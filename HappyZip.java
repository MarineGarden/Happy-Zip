package core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HappyZip {

	public static void zip( File original ) {

		byte[] fileBytes = null;
		try {
			
			fileBytes = Files.readAllBytes( original.toPath() );
			
		} catch ( IOException e ) {

			e.printStackTrace();
			
		}
		String fileName = original.getName();
		
		// remember to test multilanguage file names, like Chinese or Korean
		byte[] fileNameBytes = fileName.getBytes( StandardCharsets.UTF_8 );
		
		ArrayList<Boolean> zipBits = BiggerBiggerCluster.primeFactorsFirst( new FileBytes( fileBytes , fileNameBytes ).toBooleans() );
		
		
//		byte[] zipBytes = ByteUtility.booleansToBytes( zipBits );
//		String zipPath = original.getParent().concat( File.separator ).concat( changeFileSubname( original , "hz" ) );
//		try {
//			
//			Files.write( new File( zipPath ).toPath() , zipBytes );
//			
//		} catch ( IOException e ) {
//			
//			e.printStackTrace();
//			
//		}

	}
	
	public static class FileShell {
		
		public static File in( File f ) {
			
			String fileExtension = getFileExtension( f );
			byte[] extensionBytes = fileExtension.getBytes( StandardCharsets.UTF_8 );
			List<Byte> extensionByteList = ByteListUtility.cast( extensionBytes );
			int extensionBytesLength = extensionBytes.length;
			SerializableByteNumber extensionLengthBytes = new SerializableByteNumber( extensionBytesLength );
			List<Byte> resultData = new ArrayList<Byte>();
			resultData.addAll( extensionLengthBytes.toBytes() );
			resultData.addAll( extensionByteList );
			Path withExtensionMark = null;
			try {
				
				resultData.addAll( ByteListUtility.cast( Files.readAllBytes( f.toPath() ) ) );
				withExtensionMark = Files.write( new File( changeFileSubname( f , "hz" ) ).toPath() , ByteArrayUtility.castToSmallerOnes( resultData.toArray( new Byte[] {} ) ) );
				
				System.out.println( "OK! " + changeFileSubname( f , "hz" ) );
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return withExtensionMark.toFile();
			
		}
		
		private static String getFileExtension( File f ) {
			
			return f.getName().split( "\\." )[ 1 ];
			
		}
		
		public static File out( File withExtensionMark ) {
			
			File result = null;
			
			try {
				
				byte[] markedBytes = Files.readAllBytes( withExtensionMark.toPath() );
				List<Boolean> markedBooleans = ByteUtility.bytesToBooleans( Arrays.asList( ByteArrayUtility.castToBiggerOnes( markedBytes ) ) );
				int extensionMarkLength = SerializableFormatReader.getLengthes( markedBooleans ).get( 0 )*8;
				int extensionBytesLength = SerializableNumberReader.read( markedBooleans , 8 , 1 ).get( 0 );
				List<Boolean> markedBooleansWithoutMarkLength = markedBooleans.subList( extensionMarkLength , markedBooleans.size() );
				List<Byte> markedBytesWithoutMarkLength = Arrays.asList( ByteArrayUtility.castToBiggerOnes( ByteUtility.booleansToBytes( markedBooleansWithoutMarkLength ) ) );
				byte[] extensionBytes = ByteArrayUtility.castToSmallerOnes( markedBytesWithoutMarkLength.subList( 0 , extensionBytesLength ).toArray( new Byte[] {} ) );
				String extension = new String( extensionBytes , StandardCharsets.UTF_8 );
				byte[] fileDataBytes = ByteArrayUtility.castToSmallerOnes( markedBytesWithoutMarkLength.subList( extensionBytesLength , markedBytesWithoutMarkLength.size() ).toArray( new Byte[] {} ) );
				
				//
				System.out.println( "OK!! " + markedBooleans );
				
				String fileName = changeFileSubname( withExtensionMark , extension );
				result = new File( fileName );
				
				Files.write( result.toPath() , fileDataBytes );
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return result; 
			
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class BinaryNumber extends ArrayList<Boolean> {
		
		public BinaryNumber( int decimalNumber ) {
			
			String binaryNumber = Integer.toBinaryString( decimalNumber );
			for ( String bit : binaryNumber.split( "" ) )
				if ( bit.equals( "1" ) )
					add( true );
				else
					add( false );
			
		}
		public BinaryNumber( int number , int numberBitsLength ) {
			
			this( number );
			addAll( 0 , BooleanListUtility.toSameBooleans( numberBitsLength - size() , false ) );
			
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class BinaryNumberWithItsSize extends BinaryNumber {
		
		private final int size;
		
		public BinaryNumberWithItsSize( int number ) {
			
			super( number );
			size = size();
			
		}
		public BinaryNumberWithItsSize( int number , int numberBitsLength ) {
			
			super( number , numberBitsLength );
			size = size();
			
		}
		
		public int getSize() {
			
			return size;
			
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class BinaryNumberWithMultipleIndex extends BinaryNumberWithItsSize {
		
		private int multiple;
		
		public BinaryNumberWithMultipleIndex( int number ) {
			
			super( number );
			
		}
		public BinaryNumberWithMultipleIndex( int number , int numberBitsLength ) {
			
			super( number , numberBitsLength );
			
		}
		
		public void whenMeetUpMultiples( int multiple , Consumer<Integer> action ) {
			
			this.multiple = multiple;
			for ( int i = 0 ; i < now() + gain() ; i += multiple )
				action.accept( i );
			
		}
		
		private int now() {
			
			return getSize();
			
		}
		
		private int gain() {
			
			return getSize()/( multiple - 1 ) + ( getSize() % ( multiple - 1 ) > 0 ? 1 : 0 );
			
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class BinaryNumberWithMultipleTrueIndex extends BinaryNumberWithMultipleIndex implements Consumer<Integer> {
	
		private final int multiple;
		
		public BinaryNumberWithMultipleTrueIndex( int number , int multiple ) {
			
			super( number );
			this.multiple = multiple;
			whenMeetUpMultiples( multiple , this );
			
		}
		public BinaryNumberWithMultipleTrueIndex( int number , int multiple , int numberBitsLength ) {
			
			super( number , numberBitsLength );
			this.multiple = multiple;
			whenMeetUpMultiples( multiple , this );
			
		}

		@Override
		public void accept( Integer i ) {
			
			add( i , true );
			
		}
		
		public int getMultiple() {
			
			return multiple;
			
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class SerializableNumber extends BinaryNumberWithMultipleTrueIndex {
		
		public SerializableNumber( int number , int markableMultipleIndex ) {
			
			super( number , markableMultipleIndex );
			set( size() - getMultiple() , false );
			
		}
		public SerializableNumber( int number , int markableMultipleIndex , int numberBitsLength ) {
			
			super( number , markableMultipleIndex , numberBitsLength );
			set( size() - getMultiple() , false );
			
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class SerializableEachBitNumber extends SerializableNumber {
		
		public SerializableEachBitNumber( int number ) {
			
			super( number , 2 );
			
		}
		
	}
	
	@SuppressWarnings("serial")
	public static class SerializableByteNumber extends SerializableNumber {
		
		public SerializableByteNumber( int number ) {
			
			super( number , 8 , MathUtility.findHigherOrEquallyMultiple( new BinaryNumberWithItsSize( number ).getSize() , 7 ) );
			
		}
		
		public List<Byte> toBytes() {
			
			return ByteListUtility.cast( ByteUtility.booleansToBytes( this ) );
			
		}
		
	}
	
	public static class SerializableNumberReader {
		
		public static List<Integer> read( List<Boolean> binaryNumbers , int markableMultipleIndex , int returns ) {
			
			List<Boolean> marks = new ArrayList<Boolean>();
			List<Boolean> binaryNumbersCopy = new ArrayList<Boolean>( binaryNumbers );
			for ( int i = binaryNumbersCopy.size() - markableMultipleIndex ; i > -1 ; i -= markableMultipleIndex )
				marks.add( binaryNumbersCopy.remove( i ) );
			Collections.reverse( marks );
				
			List<Integer> result = new ArrayList<Integer>();
			List<Integer> formatOfLengthes = SerializableFormatReader.getLengthes( marks );
			List<Integer> summedUpLengthes = IntegerListUtility.sumUpOneByOneAndGraduallyIncrease( formatOfLengthes );
			Integer startIndex = 0;
			Integer endIndex = summedUpLengthes.get( 0 )*markableMultipleIndex;
			for ( int i = 0 ; i < returns ; i++ ) {
				
				result.add( read( binaryNumbers.subList( startIndex , endIndex ) , markableMultipleIndex ) );
				if ( i == returns - 1 )
					break;
				startIndex = endIndex;
				endIndex = summedUpLengthes.get( i + 1 )*markableMultipleIndex;
				
			}
			return result;
				
		}
		
		private static Integer read( List<Boolean> binaryNumberWithMarks , int markableMultipleIndex ) {
			
			List<Boolean> binaryNumber = new ArrayList<Boolean>( binaryNumberWithMarks );
			for ( int i = binaryNumber.size() - markableMultipleIndex ; i > -1 ; i -= markableMultipleIndex )
				binaryNumber.remove( i );
			return BinaryCalculator.toInteger( binaryNumber );
			
		}
		
	}
	
	public static class SerializableFormatReader {
		
		// This format includes a set of true values and ends with a false value.
		public static List<Integer> getLengthes( List<Boolean> booleans ) {
			
			List<Integer> result = new ArrayList<Integer>();
			Integer counter = 1;
			for ( int i = 0 ; i < booleans.size() ; i++ )
				if ( booleans.get( i ) )
					counter++;
				else {
					
					result.add( counter );
					counter = 1;
					
				}
			return result;
			
		}
		
	}
	
	public static class BinaryCalculator {
		
		public static Integer toInteger( List<Boolean> binaryNumber ) {
			
			return Integer.valueOf( new BigInteger( new BinaryDigitsPrinter( binaryNumber ).toString() , 2 ).intValue() );
			
		}
		
	}
	
	public static class BinaryDigitsPrinter implements Consumer<Boolean> {
		
		private StringBuilder printer = new StringBuilder();
		
		public BinaryDigitsPrinter( List<Boolean> binaryNumber ) {
			
			binaryNumber.forEach( this );
			
		}

		@Override
		public void accept( Boolean b ) {
			
			printer.append( b ? "1" : "0" );
			
		}

		@Override
		public String toString() {
			
			return printer.toString();
			
		}
		
	}
	
	public static class FileBytes extends ArrayList<Byte> {
		
		public FileBytes( byte[] fileBytes ) {
			
			for( byte fileByte : fileBytes )
				add( fileByte );
			
		}
		// file name after data
		public FileBytes( byte[] fileBytes , byte[] fileNameBytes ) {
			
			this( fileBytes );
			for( byte fileNameByte : fileNameBytes )
				add( fileNameByte );
			
		}
		
		public ArrayList<Boolean> toBooleans() {
			
			return ByteUtility.bytesToBooleans( this );
			
		}
		
	}
	
	public static class MathUtility {
		
		public static int findHigherOrEquallyMultiple( int target , int multiple ) {
			
			return multiple*( target/multiple + ( target % multiple == 0 ? 0 : 1 ) );
			
		}
		
	}
	
	public static class ByteUtility {
		
		public static ArrayList<Boolean> bytesToBooleans( List<Byte> bytes ) {
			
			ArrayList<Boolean> result = new ArrayList<Boolean>();
			for ( Byte aByte : bytes ) {
				
				ArrayList<Boolean> booleansInOneByte = byteToBooleans( aByte );
				result.addAll( booleansInOneByte );
				
			}			
			return result;
			
		}
		
		public static ArrayList<Boolean> byteToBooleans( byte anyByte ) {
			
			int adjustedByte = anyByte + 128;
			String falseByteBinary = Integer.toBinaryString( adjustedByte );
			String trueByteBinary = "";
			if ( falseByteBinary.length() < 8 )
				trueByteBinary = "0".repeat( 8 - falseByteBinary.length() ).concat( falseByteBinary );
			else
				trueByteBinary = falseByteBinary;
			ArrayList<Boolean> booleans = new ArrayList<Boolean>();
			for ( String bit : trueByteBinary.split( "" ) )
				booleans.add( bit.equals( "1" ) ? true : false );
			return booleans;
			
		}
		
		public static byte[] booleansToBytes( List<Boolean> booleans ) {
			
			byte[] result = new byte[ booleans.size()/8 + 2 ];
			if ( booleans.size() > 8 ) {
				
				for ( int i = 0 ; i < booleans.size()/8 ; i++ ) {
					
					int rangeStart = i*8;
					ArrayList<Boolean> inRangeBooleans = new ArrayList<Boolean>();
					inRangeBooleans.addAll( booleans.subList( rangeStart , rangeStart + 8 ) );
					try {
						
						result[ i ] = calculateBooleansToByte( inRangeBooleans );
						
					} catch (ByteFormatException e) {
						
						e.printStackTrace();
						
					}
					
				}
				
			}
			ArrayList<Boolean> endBooleans = new ArrayList<Boolean>();
			endBooleans.addAll( booleans.subList( booleans.size() - ( booleans.size()%8 ) , booleans.size() ) );
			byte[] endBytes = booleansToByte( endBooleans );
			result[ booleans.size()/8 ] = endBytes[ 0 ];
			result[ booleans.size()/8 + 1 ] = endBytes[ 1 ];
			return result;
			
		}
		
		// for bits less than 8 ; usually suited for end bits
		// result contains two bytes, and its second byte is the format
		public static byte[] booleansToByte( ArrayList<Boolean> booleans ) {
			
			byte[] result = new byte[ 2 ];
			
			if ( booleans.size() > 8 )
				return new byte[ 0 ];
			else {
				
				Collections.reverse( booleans );
				int originalBooleanListSize = booleans.size();
				int complement = 8 - booleans.size();
				for ( int i = 0 ; i < complement ; i++ )
					booleans.add( false );
				try {
					
					Collections.reverse( booleans );
					result[ 0 ] = calculateBooleansToByte( booleans );
					
				} catch ( ByteFormatException e ) {
					
					e.printStackTrace();
					
				}
				ArrayList<Boolean> byteFormat = new ArrayList<Boolean>();
				for ( int i = 0 ; i < originalBooleanListSize ; i++ )
					byteFormat.add( true );
				for ( int i = 0 ; i < complement ; i++ )
					byteFormat.add( false );
				try {
					
					result[ 1 ] = calculateBooleansToByte( byteFormat );
					
				} catch (ByteFormatException e) {
					
					e.printStackTrace();
					
				}
				
			}
			return result;
			
		}
		
		private static byte calculateBooleansToByte( ArrayList<Boolean> booleans ) throws ByteFormatException {
			
			if ( booleans.size() == 8 ) {
				
				int result = 0;
				for ( boolean thisBoolean : booleans ) {
					
					result *= 2;
					if ( thisBoolean )
						result += 1;
					
				}
				result -= 128;
				return (byte) result;
				
			} else
				throw new ByteFormatException();
			
		}
		
		private static class ByteFormatException extends Exception {

			@Override
			public String getMessage() {

				return "this byte must contains 8 bits ; the quantity of bits does not match";
				
			}
			
		}
		
	}
	
	public static class ByteArrayUtility {
		
		public static byte[] castToSmallerOnes( Byte[] bytes ) {
			
			byte[] result = new byte[ bytes.length ];
			for ( int i = 0 ; i < result.length ; i++ )
				result[ i ] = bytes[ i ];
			return result;
			
		}
		
		public static Byte[] castToBiggerOnes( byte[] bytes ) {
			
			Byte[] result = new Byte[ bytes.length ];
			for ( int i = 0 ; i < result.length ; i++ )
				result[ i ] = bytes[ i ];
			return result;
			
		}
		
	}
	
	public static class BigIntegerUtility {
		
		public static List<Boolean> toBinaryBooleans( BigInteger number ) {
			
			List<Boolean> result = new ArrayList<Boolean>();
			for ( String bit : number.toString( 2 ).split( "" ) )
				if ( bit.equals( "1" ) )
					result.add( true );
				else
					result.add( false );
			return result;
			
		}
		
	}
	
	public static class ListUtility {
		
		public static <T> boolean hasIt( T it , List<T> list ) {
			
			for ( T element : list )
				if ( element.equals( it ) )
					return true;
			return false;
			
		}
		
		public static <T> void changeLast( T newValue , List<T> list ) {
			
			list.set( list.size() - 1 , newValue );
			
		}
		
	}
	
	public static class BooleanListUtility {
		
		public static List<Boolean> flip( List<Boolean> original ) {
			
			ArrayList<Boolean> result = new ArrayList<Boolean>( original );
			for ( int i = 0 ; i < result.size() ; i++ )
				result.set( i , ! result.get( i ) );
			return result;
			
		}
		
		public static List<Boolean> reverse( List<Boolean> original ) {
			
			List<Boolean> result = new ArrayList<Boolean>( original );
			Collections.reverse( result );
			return result;
			
		}
		
		public static List<Boolean> toSameBooleans( int length , boolean type ) {
			
			List<Boolean> booleans = new ArrayList<Boolean>();
			BinaryIterator assembler = new BinaryIterator() {
				
				@Override
				public void timesTwo() {

					booleans.addAll( booleans );
					
				}
				
				@Override
				public void addsOne() {

					booleans.add( type );
					
				}
				
				public List<Boolean> getBooleans() {
					
					return booleans;
					
				}
				
			};
			assembler.fromInteger( length );
			return booleans;
			
		}
		
		public static List<Boolean> cutEndBooleans( boolean valueType , List<Boolean> original ) {
			
			LinkedList<Boolean> result = new LinkedList<Boolean>( original );
			while ( result.getLast() == valueType )
				result.removeLast();
			return result;
			
		}
		
		public static boolean areTheSame( List<Boolean> booleans ) {
			
			boolean firstBoolean = booleans.get( 0 );
			for ( int i = 0 ; i < booleans.size() ; i++ )
				if ( booleans.get( i ) != firstBoolean )
					return false;
			return true;
			
		}
		
	}
	
	public static class ByteListUtility {
		
		public static List<Byte> cast( byte[] bytes ) {
			
			List<Byte> result = new ArrayList<Byte>();
			for ( byte oneByte : bytes )
				result.add( oneByte );
			return result;
			
		}
		
	}
	
	public static class IntegerListUtility {
		
		public static List<Integer> sumUpOneByOneAndGraduallyIncrease( List<Integer> values ) {
			
			List<Integer> result = new ArrayList<Integer>();
			Integer sum = 0;
			for ( Integer value : values )
				result.add( sum += value );
			return result;
			
		}
		
	}
	
	public static class PixelListUtility {
		
		public static List<AIframe.Bubbles.Bubble.CircleStructure.Pixel> removeLaterSamePlace( List<AIframe.Bubbles.Bubble.CircleStructure.Pixel> pixels ) {
			
			List<AIframe.Bubbles.Bubble.CircleStructure.Pixel> result = new ArrayList<AIframe.Bubbles.Bubble.CircleStructure.Pixel>();
			for ( AIframe.Bubbles.Bubble.CircleStructure.Pixel pixel : pixels )
				if ( ! samePlaceOccurs( pixel , result ) )
					result.add( pixel );
			return result;
			
		}
		
		private static boolean samePlaceOccurs( AIframe.Bubbles.Bubble.CircleStructure.Pixel maybe , List<AIframe.Bubbles.Bubble.CircleStructure.Pixel> all ) {
			
			for ( AIframe.Bubbles.Bubble.CircleStructure.Pixel pixel : all )
				if ( hasSamePlace( maybe , pixel ) )
					return true;
			return false;
			
		}
		
		private static boolean hasSamePlace( AIframe.Bubbles.Bubble.CircleStructure.Pixel a , AIframe.Bubbles.Bubble.CircleStructure.Pixel b ) {
			
			return a.x == b.x && a.y == b.y;
			
		}
		
	}
	
	public static class ImageUtility {
		
		public static Image getImage( JComponent component ) {
			
			BufferedImage ret = new BufferedImage(component.getWidth(), component.getHeight() , BufferedImage.TYPE_INT_RGB );
		    Graphics g = ret.getGraphics();
		    component.paint(g);
		    g.dispose();
		    return ret;
			
		}
		
	}
	
	public static class ColorUtility {
		
		public static String code( Color c ) {
			
			return "(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "," + c.getAlpha() + ")";
			
		}
		
		public static Color randomColor() {
			
			int r = randomColorPart();
			int g = randomColorPart();
			int b = randomColorPart();
			return new Color( r , g , b );
			
		}
		
		private static int randomColorPart() {
			
			return (int)( 0x100*Math.random() );
			
		}
		
	}
	
	public static interface BinaryIterator {
		
		void timesTwo();
		void addsOne();
		default void fromInteger( int i ) {
					
			for ( String bit : Integer.toBinaryString( i ).split( "" ) ) {
				
				timesTwo();
				if ( bit.equals( "1" ) )
					addsOne();
					
			}
			
		}
		
	}
	
	public static class BitsInteger {
		
		private static final BigInteger ONE = new BigInteger( "1" );
		private static final BigInteger TWO = new BigInteger( "2" );
		
		private ArrayList<Boolean> fileBits = new ArrayList<Boolean>();
		private boolean startsWithTrue = true;
		
		public BitsInteger( FileBytes fileBytes ) {
			
			fileBits.addAll( fileBytes.toBooleans() );
			
			// the test has not reached yet, must be checked again
			if ( fileBits.size() > 0 )
				if ( ! fileBits.get( 0 ) ) {
					
					startsWithTrue = false;
					flipBits( fileBits );
					
				}
			
		}
		public BitsInteger( List<Boolean> fileBits ) {
			
			this.fileBits.addAll( fileBits );
			
			// here has not reached yet, must be checked again
			if ( fileBits.size() > 0 )
				if ( ! fileBits.get( 0 ) ) {
					
					startsWithTrue = false;
					flipBits( fileBits );
					
				}
			
		}
		
		// here has not reached yet, must be checked again
		private static void flipBits( List<Boolean> bits ) {
			
			for ( int i = 0 ; i < bits.size() ; i++ ) {
				
				bits.set( i , ! bits.get( i ) );
				
			}
			
		}
		
		public BigInteger toInteger() {
			
			BigInteger result = new BigInteger( "0" );			
			for ( Boolean fileBit : fileBits ) {
				
				result = result.multiply( TWO );
				if ( fileBit )
					result = result.add( ONE );
				
			}
			return result;
			
		}
		
		public boolean startsWithTrue() {
			
			return startsWithTrue;
			
		}
		
	}
	
	public static class PrimeNumber {
	
		private static final BigInteger ZERO = new BigInteger( "0" );
		private static final BigInteger ONE = new BigInteger( "1" );
		private static final BigInteger TWO = new BigInteger( "2" );
		
		private final ArrayList<BigInteger> primeNumbers = new ArrayList<BigInteger>();
		
		public PrimeNumber() {
			
			if ( primeNumbers.size() == 0 )
				primeNumbers.add( TWO );
			
		}
		
		public BigInteger next() {
			
			BigInteger biggerThanLastPrimeNumber = primeNumbers.get( primeNumbers.size() - 1 ).add( ONE );
			while ( ! isPrimeNumber( biggerThanLastPrimeNumber ) )
				biggerThanLastPrimeNumber = biggerThanLastPrimeNumber.add( ONE );
			primeNumbers.add( biggerThanLastPrimeNumber );
			return biggerThanLastPrimeNumber;
			
		}
		
		private boolean isPrimeNumber( BigInteger number ) {
			
			for ( BigInteger primeNumber : primeNumbers ) {
				
				if ( number.mod( primeNumber ).compareTo( ZERO ) == 0 )
					return false;
				
			}
			return true;
			
		}
		
	}
	
	public static class PrimeFactorFormat {
		
		private static final BigInteger ZERO = new BigInteger( "0" );
		private static final BigInteger ONE = new BigInteger( "1" );
		private static final BigInteger TWO = new BigInteger( "2" );
		
		private final ArrayList<BigInteger> primeNumbers = new ArrayList<BigInteger>();
		private final ArrayList<BigInteger> exponents = new ArrayList<BigInteger>();
		
		private final PrimeNumber calculator = new PrimeNumber();
		
		public PrimeFactorFormat( BigInteger number ) throws PrimeFactorFormatException {
			
			primeNumbers.add( TWO );
			if ( number.compareTo( TWO ) > -1 ) {
				
				while ( number.compareTo( ONE ) != 0 ) {
					
					BigInteger lastPrimeNumber = primeNumbers.get( primeNumbers.size() - 1 );
					BigInteger exponent = getExponent( number , lastPrimeNumber );
					exponents.add( exponent );
					BigInteger lastPrimeNumberWithExponent = lastPrimeNumber.pow( exponent.intValue() );
					number = number.divide( lastPrimeNumberWithExponent );
					
					if ( number.compareTo( ONE ) != 0 )
						primeNumbers.add( calculator.next() );
					
				}
				
			} else {
				
				throw new PrimeFactorFormatException();
				
			}
			
		}
		
		private BigInteger getExponent( BigInteger target , BigInteger primeNumber ) {
			
			BigInteger result = new BigInteger( "0" );
			while ( target.mod( primeNumber ).compareTo( ZERO ) == 0 ) {
				
				result = result.add( ONE );
				target = target.divide( primeNumber );
				
			}
			return result;
			
		}
		
		public ArrayList<BigInteger> getPrimeNumbers() {
			
			return primeNumbers;
			
		}
		
		public ArrayList<BigInteger> getUsedPrimeNumbers() {
			
			ArrayList<BigInteger> result = new ArrayList<BigInteger>();
			for ( int i = 0 ; i < primeNumbers.size() ; i++ )
				if ( exponents.get( i ).compareTo( ZERO ) != 0 )
					result.add( primeNumbers.get( i ) );
			
			return result;
			
		}

		public ArrayList<BigInteger> getExponents() {
			
			return exponents;
			
		}
		
		public ArrayList<BigInteger> getUsedPrimeNumberExponents() {
			
			ArrayList<BigInteger> result = new ArrayList<BigInteger>();
			ArrayList<BigInteger> usedPrimeNumbers = getUsedPrimeNumbers();
			for ( int i = 0 ; i < usedPrimeNumbers.size() ; i++ ) {
				
				for ( int j = 0 ; j < primeNumbers.size() ; j++ )
					if ( usedPrimeNumbers.get( i ).compareTo( primeNumbers.get( j ) ) == 0 ) {
						
						result.add( exponents.get( j ) );
						break;
						
					}
				
			}
			return result;
			
		}
		
		public List<Boolean> toBooleans() {
			
			List<Boolean> result = new ArrayList<Boolean>();
			int iterator = 0;
			for ( BigInteger possiblePrimeFactor : getPrimeNumbers() ) {
				
				if ( ListUtility.hasIt( possiblePrimeFactor , getUsedPrimeNumbers() ) ) {
					
					int length = getUsedPrimeNumberExponents().get( iterator ).intValue();
					result.addAll( BooleanListUtility.toSameBooleans( length , true ) );
					
				} else
					result.add( false );
					
				iterator++;
				
			}
			return result;
			
		}
		
		public static BigInteger toInteger( ArrayList<BigInteger> primeFactorExponents ) {
			
			BigInteger result = ZERO;
			PrimeNumber calculator = new PrimeNumber();
			result.add( TWO.pow( primeFactorExponents.get( 0 ).intValue() ) );
			primeFactorExponents.remove( 0 );
			for ( BigInteger primeFactor : primeFactorExponents )
				result = result.multiply( calculator.next().pow( primeFactor.intValue() ) );
			return result;
			
		}

		private static class PrimeFactorFormatException extends Exception {

			@Override
			public String getMessage() {

				return "must larger than 1 ; this number has no prime numbers";
				
			}
			
		}
		
	}
	
	public static class ZipSeedMaker {
		
		public static ArrayList<Boolean> make( BitsInteger fileInteger ) {
			
			PrimeFactorFormat filePrimeFactorFormat = null;
			try {
				
				filePrimeFactorFormat = new PrimeFactorFormat( fileInteger.toInteger() );
				
			} catch (HappyZip.PrimeFactorFormat.PrimeFactorFormatException e) {
				
				e.printStackTrace();
				
			}
			ArrayList<Boolean> result = new ArrayList<Boolean>();
			for ( int i = 0 ; isNotGreaterThanTWOsExponent( i , filePrimeFactorFormat ) ; i++ )
				result.add( fileInteger.startsWithTrue() );
			return result;
			
		}
		
		private static boolean isNotGreaterThanTWOsExponent( int target , PrimeFactorFormat format ) {
			
			return new BigInteger( Integer.toString( target ) ).compareTo( format.getExponents().get( 0 ) ) < 1;
			
		}
		
	}
	
	public static class FileBytesClusterPreparation {
		
		private final BitsInteger fileSymbol;
		private PrimeFactorFormat symbolAnalysis = null;
		
		public FileBytesClusterPreparation( FileBytes fileBytes ) {
			
			fileSymbol = new BitsInteger( fileBytes );
			try {
				
				symbolAnalysis = new PrimeFactorFormat( fileSymbol.toInteger() );
				
			} catch ( HappyZip.PrimeFactorFormat.PrimeFactorFormatException e ) {
				
				e.printStackTrace();
				
			}
			
		}
		public FileBytesClusterPreparation( BitsInteger fileSymbol ) {
			
			this.fileSymbol = fileSymbol;
			try {
				
				symbolAnalysis = new PrimeFactorFormat( this.fileSymbol.toInteger() );
				
			} catch ( HappyZip.PrimeFactorFormat.PrimeFactorFormatException e ) {
				
				e.printStackTrace();
				
			}
			
		} 
		
		public ArrayList<Boolean> getSeed() {
			
			return ZipSeedMaker.make( fileSymbol );
			
		}
		
		public ArrayList<BigInteger> getExponentsFromSymbol() {
			
			return symbolAnalysis.getExponents();
			
		}
		
	}
	
	public static class CalculatorOfOne {
		
		public static List<Boolean> add( List<Boolean> binary ) {
			
			Collections.reverse( binary );
			for ( int i = 0 ; i < binary.size() ; i++ ) {
				
				if ( binary.get( i ) )
					binary.set( i , false );
				else {
					
					binary.set( i , true );
					break;
					
				}
				
			}
			Collections.reverse( binary );
			return binary;
			
		}
		
		public static ArrayList<Boolean> subtract( ArrayList<Boolean> binary ) {
			
			Collections.reverse( binary );
			for ( int i = 0 ; i < binary.size() ; i++ ) {
				
				if ( binary.get( i ) ) {
					
					binary.set( i , false );
					break;
					
				} else
					binary.set( i , true );
				
			}
			Collections.reverse( binary );
			return binary;
			
		}
		
	}
	
	public static class FirstTrue {
		
		private boolean mark = false;
		
		public ArrayList<Boolean> cut( List<Boolean> booleans ) {
			
			ArrayList<Boolean> result = new ArrayList<Boolean>();
			for ( Boolean oneBoolean : booleans ) {
				
				if ( mark )
					result.add( oneBoolean );
				
				ifTrueThenMarkIt( oneBoolean );
				
			}
			return result;
			
		}
		
		private void ifTrueThenMarkIt( boolean target ) {
			
			if ( target )
				mark = true;
			
		}
		
		public ArrayList<Boolean> append( List<Boolean> binary , int futureLength ) {
			
			ArrayList<Boolean> result = new ArrayList<Boolean>();
			int falsesAhead = futureLength - binary.size() - 1;
			for ( int i = 0 ; i < falsesAhead ; i++ )
				result.add( false );
			result.add( true );
			result.addAll( binary );
			return result;
			
		}
		
	}
	
	public static class Cluster {
		
//		001     0
//		010     1
//		011   00
//		100   01
//		101   10
//		110   11
		
		public static ArrayList<Boolean> cluster( List<Boolean> binary ) {
			
			List<Boolean> oneAdded = CalculatorOfOne.add( binary );
			return new FirstTrue().cut( oneAdded );
			
		}
		
		public static ArrayList<Boolean> uncluster( List<Boolean> binary , int futureLength ) {
			
			return CalculatorOfOne.subtract( new FirstTrue().append( binary , futureLength ) );
			
		}
		
	}
	
	public static class OneBitCluster {
		
		public static OneBitClusterResult cluster( ArrayList<Boolean> plant ) {
			
			ArrayList<Boolean> clusterTry = Cluster.cluster( plant );
			if ( clusterTry.size() == plant.size() - 1 )
				return new OneBitClusterResult( clusterTry , true );
			else
				return new OneBitClusterResult( Cluster.uncluster( clusterTry , plant.size() - 1 ) , false );
			
		}
		
		public static ArrayList<Boolean> uncluster( List<Boolean> plant , boolean operation ) {
			
			if ( operation ) {
				
				return Cluster.uncluster( plant , plant.size() + 1 );
				
			} else {
				
				ArrayList<Boolean> plantCluster = Cluster.cluster( plant );
				return Cluster.uncluster( plantCluster , plant.size() + 1 );
				
			}
			
		}
		
	}
	
	public static class OneBitClusterResult {
		
		private final ArrayList<Boolean> cluster;
		private final boolean operation;
		
		public OneBitClusterResult( ArrayList<Boolean> cluster , boolean operation ) {
			
			this.cluster = cluster;
			this.operation = operation;
			
		}

		public ArrayList<Boolean> getCluster() {
			
			return cluster;
			
		}

		public boolean getOperation() {
			
			return operation;
			
		}

		@Override
		public String toString() {

			return cluster.toString().concat( "," ).concat( Boolean.toString( operation ) );
			
		}
		
	}
	
	public static class Clusters extends ArrayList<Boolean> {
		
		public Clusters( ArrayList<Boolean> original ) {
			
			if ( BooleanListUtility.areTheSame( original ) )
				addAll( original );
			else {
				
				PathAndCore path_core = findPathAndCore( original );
				addAll( path_core.core );
				if ( get( 0 ) )
					BooleanListUtility.flip( path_core.path );
				addAll( path_core.path );
				
			}
			
		}
		
		public List<Boolean> cluster( Direction transformMethod ) throws HappyZip.PrimeFactorFormat.PrimeFactorFormatException {
			
			List<Boolean> withoutEndingFalseOperations = BooleanListUtility.cutEndBooleans( false , this );
			int endingFalseOperations = size() - withoutEndingFalseOperations.size();
			if ( transformMethod.name().equals( "REFACTOR_TO_PRIME_FORMAT" ) ) {
				
				List<Boolean> primeFactorBooleans = new PrimeFactorFormat( new BitsInteger( withoutEndingFalseOperations ).toInteger() ).toBooleans();
				primeFactorBooleans.addAll( BooleanListUtility.toSameBooleans( endingFalseOperations , false ) );
				primeFactorBooleans = BooleanListUtility.reverse( primeFactorBooleans );
				return primeFactorBooleans;
				
			} else {
				
				PrimeFactorFormat primeFactorFormat = new PrimeFactorFormat( new BitsInteger( withoutEndingFalseOperations ).toInteger() );
				BigInteger newClusterNumber = PrimeFactorFormat.toInteger( primeFactorFormat.getExponents() );
				List<Boolean> newClusterBooleans = BigIntegerUtility.toBinaryBooleans( newClusterNumber );
				newClusterBooleans.addAll( BooleanListUtility.toSameBooleans( endingFalseOperations , false ) );
				newClusterBooleans = BooleanListUtility.reverse( newClusterBooleans );
				return newClusterBooleans;
				
			}
			
		}
		
		public List<Boolean> uncluster() {
			
			FileBytesClusterPreparation loader = new FileBytesClusterPreparation( new BitsInteger( this ) );
			List<Boolean> seed = loader.getSeed();
			int seedSize = seed.size();
			List<Boolean> path = new ArrayList<Boolean>( this ).subList( seedSize , size() );
			for ( int i = 0 ; i < path.size() ; i++ )
				seed = OneBitCluster.uncluster( seed , path.get(i) );
			return seed;
			
		}
		
		private PathAndCore findPathAndCore( ArrayList<Boolean> source ) {
			
			PathAndCore result = new PathAndCore();
			OneBitClusterResult buffer;
			while( ! BooleanListUtility.areTheSame( ( buffer = OneBitCluster.cluster( source ) ).getCluster() ) ) {
				
				result.path.add( buffer.operation );
				source = buffer.cluster;
				
			}
			result.core.addAll( source );
			return result;
			
		}
		
		private class PathAndCore {
			
			private ArrayList<Boolean> path = new ArrayList<Boolean>();
			private ArrayList<Boolean> core = new ArrayList<Boolean>();
			
		}
		
		public enum Direction {
			
			REFACTOR_TO_PRIME_FORMAT,
			TO_INTEGER
			
		}
		
	}
	
	public static class PrimeFactorFormatTranslator {
		
		private static final BigInteger ZERO = new BigInteger( "0" );
		private static final BigInteger ONE = new BigInteger( "1" );
		
//		public static ArrayList<Boolean> removeFalseOperationAtEnd( ArrayList<Boolean> bits ) {
//			
//			OneBitClusterLoop result = new OneBitClusterLoop( bits );
//			while ( ! isFinalClusterTrue( result.loop().getResult().getCluster() ) )
//				result = (OneBitClusterLoop)result.loop();
//			return OneBitCluster.uncluster( result.getResult().getCluster() , true );
//			
//		}
		
		public static ArrayList<Boolean> addFalseOperationAtEnd( ArrayList<Boolean> bits , BigInteger operationCount ) {
			
			while ( operationCount.compareTo( ZERO ) != 0 ) {
				
				bits = OneBitCluster.uncluster( bits , false );
				operationCount = operationCount.subtract( ONE );
				
			}
			return bits;
			
		}
		
	}
	
	public static class EndBitOperationMark {
		
		public static ArrayList<Boolean> markTrueAtFirst( ArrayList<Boolean> clusteredBits ) {
			
			LinkedList<Boolean> result = new LinkedList<Boolean>( clusteredBits );
			result.addFirst( true );
			return new ArrayList<Boolean>( result );
			
		}
		
		public static ArrayList<Boolean> markFalseAtFirst( ArrayList<Boolean> clusteredBits ) {
			
			LinkedList<Boolean> result = new LinkedList<Boolean>( clusteredBits );
			result.addFirst( false );
			return new ArrayList<Boolean>( result );
			
		}
		
		public static ArrayList<Boolean> remove( ArrayList<Boolean> clusteredBits ) {
			
			return new ArrayList<Boolean>( clusteredBits.subList( 1 , clusteredBits.size() ) );
			
		}
		
	}
	
	public static class BiggerBiggerCluster {
		
		private static final BigInteger ZERO = new BigInteger( "0" );
		private static final BigInteger ONE = new BigInteger( "1" );
		private static final BigInteger TWO = new BigInteger( "2" );
		
		public static ArrayList<Boolean> clustersFirst( ArrayList<Boolean> fileBits ) {
			
			ArrayList<Boolean> clustered = new ArrayList<Boolean>();
			ClusterAllResult i = clusterAll( fileBits );
			clustered.addAll( i.getClusteredFileBits() );
			if ( ! fileBits.get( 0 ) )
				clustered.set( clustered.size() - 1 , true );
			ArrayList<BigInteger> primeFactors = toPrimeFactors( clustered );
			BigInteger fileSymbol = PrimeFactorFormat.toInteger( primeFactors );
			ArrayList<Boolean> newFileBits = toBooleans( fileSymbol );
			if ( i.needsToBeFlipped() )
				BitsInteger.flipBits( newFileBits );
			if ( fileBits.get( 0 ) )
				return EndBitOperationMark.markTrueAtFirst( newFileBits );
			return EndBitOperationMark.markFalseAtFirst( newFileBits );
			
		}
		
		public static class ClusterAllResult {
			
			private final boolean needsToBeFlipped;
			private final ArrayList<Boolean> clusteredFileBits;
			
			public ClusterAllResult( boolean needsToBeFlipped , ArrayList<Boolean> clusteredFileBits ) {
				
				this.needsToBeFlipped = needsToBeFlipped;
				this.clusteredFileBits = clusteredFileBits;
				
			}

			public boolean needsToBeFlipped() {
				return needsToBeFlipped;
			}

			public ArrayList<Boolean> getClusteredFileBits() {
				return clusteredFileBits;
			}
			
		}
		
		private static ClusterAllResult clusterAll( ArrayList<Boolean> fileBits ) {
			
			fileBits = EndBitOperationMark.remove( fileBits );
			LinkedList<Boolean> operations = new LinkedList<Boolean>();
			while ( ! BooleanListUtility.areTheSame( fileBits ) ) {
				
				OneBitClusterResult cluster = OneBitCluster.cluster( fileBits );
				operations.addFirst( cluster.getOperation() );
				fileBits = cluster.getCluster();
				
			}
			boolean needsToBeFlipped = ( ! fileBits.get( 0 ) );
			fileBits.addAll( operations );
			return new ClusterAllResult( needsToBeFlipped , fileBits );
			
		}
		
		private static ArrayList<Boolean> toBooleans( BigInteger number ) {
			
			ArrayList<Boolean> result = new ArrayList<Boolean>();
			while ( number.compareTo( ZERO ) != 0 ) {
				
				if ( number.compareTo( ONE ) == 1 ) {
					
					result.add( number.mod( TWO ).intValue() == 1 );
					number = number.divide( TWO );
					
				} else
					result.add( number.intValue() == 1 );
				
			}
			return result;
			
		}
		
		private static ArrayList<BigInteger> toPrimeFactors( ArrayList<Boolean> booleans ) {
			
			ArrayList<BigInteger> result = new ArrayList<BigInteger>();
			BigInteger counter = ZERO;
			for ( Boolean trueOneOrFalseZero : booleans )
				if ( trueOneOrFalseZero ) {
					
					counter = counter.add( ONE );
					
				} else {
					
					result.add( counter );
					counter = ZERO;
					result.add(ZERO);
					
				}
			if ( counter.compareTo( ZERO ) != 0 )
				result.add( counter );
			return result;
			
		}
		
		public static ArrayList<Boolean> primeFactorsFirst( ArrayList<Boolean> fileBits ) {
			
			System.out.println( fileBits );
			
			
			fileBits = EndBitOperationMark.remove( fileBits );
			FileBytesClusterPreparation preparation = new FileBytesClusterPreparation( new BitsInteger( fileBits ) );
			ArrayList<Boolean> seed = preparation.getSeed();
			if ( preparation.getExponentsFromSymbol().size() > 1 ) {
			
				for ( int i = 1 ; i < preparation.getExponentsFromSymbol().size() ; i++ ) {
					
					BigInteger exponent = preparation.getExponentsFromSymbol().get( i );
					
					if ( exponent.compareTo( ZERO ) == 1 ) {
						
						while ( exponent.compareTo( ZERO ) != 0 ) {
							
							seed = OneBitCluster.uncluster( seed , true );
							exponent = exponent.subtract( ONE );
							
						}
						
					} else
						seed = OneBitCluster.uncluster( seed , false );
					
				}
				
			}
			return EndBitOperationMark.markTrueAtFirst( seed );
			
		}
		
		public static class ClusterException extends Exception {

			@Override
			public String getMessage() {

				return "end of false cluster operation ; final cluster must use true operation to assemble prime factors";
				
			}
			
		}
		
	}
	
	public static boolean isFinalClusterTrue( ArrayList<Boolean> fileBits ) {
		
		return Cluster.cluster( fileBits ).size() == fileBits.size() - 1 ;
		
	}
	
	public static ArrayList<Boolean> addFinalClusterMark( ArrayList<Boolean> fileBits ) {
		
		LinkedList<Boolean> result = new LinkedList<Boolean>( fileBits );
		if ( isFinalClusterTrue( fileBits ) )
			result.addFirst( true );
		else
			result.addFirst( false );
		return new ArrayList<Boolean>( result );
		
	}
	
	public static String changeFileSubname( File target , String newSubname ) {
		
		String fileName = target.getPath();
		String fileNameWithoutExtension = fileName.split( "\\." )[0];
		String newFileName = fileNameWithoutExtension.concat( "." ).concat( newSubname );
		return newFileName;
		
	}
	
	public static class ImageCreator extends BufferedImage {
		
		public interface HowToPaint {

			void draw( int x , int y , Graphics g );
			
		}
		
		public ImageCreator( int width , int height , HowToPaint how ) {
			
			super( width , height , BufferedImage.TYPE_INT_ARGB );
			draw( width , height , how );
			
		}
		
		public void draw( int width , int height , HowToPaint how ) {
			
			for ( int x = 0 ; x < width ; x++ )
				for ( int y = 0 ; y < height ; y++ )
					how.draw( x , y , getGraphics() );
			
		}
		
		public void draw( Component toShow , HowToPaint how ) {
			
			draw( 100 , 100 , how );
			Graphics graphics = toShow.getGraphics();
			graphics.drawImage( this , 0 , 0 , toShow );
			
		}
		
	}
	
	public static class AIframe {
		
		private static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
		private static final Dimension FRAME_SIZE = new Dimension( SCREEN_SIZE.width/2 , SCREEN_SIZE.height/2 );
		private static final Point FRAME_LOCATION = new Point( FRAME_SIZE.width/2 , FRAME_SIZE.height/2 );
		
		private final PlainBoard canvas = new PlainBoard();
		
		public Frame getCanvas() {
			
			return canvas;
			
		}
		
		public BufferedImage getImage() {
			
			return (BufferedImage)( canvas.createImage( canvas.getWidth() , canvas.getHeight() ) );
			
		}
		
		public static class PlainBoard extends JFrame {
			
			public PlainBoard() {
				
				getContentPane().setBackground( new Color( 250 , 198 , 104 ) );
				setBounds( new Rectangle( FRAME_LOCATION , FRAME_SIZE ) );
				setLayout( null );
				setUndecorated( true );
				setDefaultCloseOperation( EXIT_ON_CLOSE );
				
				addMouseMotionListener( new Draggable( this ) );
				addMouseListener( new ExitControl() );
				ShiningHint hint = new ShiningHint( new BottomRightLabel( this , "Double click to exit." , new Font( "Calibri" , Font.PLAIN , 30 ) , Color.BLACK ) );
				
				setVisible( true );
				
			}
			
			public static class Draggable extends MouseMotionAdapter {

				private final Component draggable;
				private Point press;
					
				public Draggable( Component draggable ) {
						
					this.draggable = draggable;
						
				}
					
				@Override
				public void mouseMoved( MouseEvent event ) {
					
					press = null;
						
				}

				@Override
				public void mouseDragged( MouseEvent event ) {
					
					if ( press == null )
						press = event.getPoint();
					if ( ! draggable.getLocation().toString().equals( event.getLocationOnScreen().toString() ) ) {
						
						Point drag = event.getLocationOnScreen();
						draggable.setLocation( drag.x - press.x , drag.y - press.y );
						
					}
					
				}
				
				
					
			}
			
			public static class ExitControl extends MouseAdapter {

				@Override
				public void mousePressed( MouseEvent event ) {
					
					if ( event.getClickCount() > 1 )
						System.exit( 0 );
					
				}
				
			}
			
			public static class JLabelGadget extends JLabel {
				
				public JLabelGadget( Container container , int x , int y , int textSize , int style , String fontName ) {
					
					container.add( this );
					
					setBounds( x , y , 0 , textSize );
					setFont( new Font( fontName , style , textSize ) );
					
					container.repaint();
					
				}
				public JLabelGadget( Container container , String text , Font font , Color color ) {
					
					this( container , 0 , 0 , font.getSize() , font.getStyle() , font.getName() );
					setText( text );
					setForeground( color );
					
				}

				@Override
				public void setText( String text ) {
					super.setText( text );
					
					setSize( getMaximumSize().width , getMaximumSize().height );
					
				}

			}
			
			public static class BottomRightLabel extends JLabelGadget {

				public BottomRightLabel( Container container , String text , Font font , Color color ) {
					super( container , text , font , color );
					
					int newX = container.getWidth() - getWidth();
					int newY = container.getHeight() - getHeight();
					setLocation( newX , newY );
					
				}
				
			}
			
			public static class SineWave {
				
				private final double start;
				private final double height;
				
				public SineWave( double start , double height ) {
					
					this.start = start;
					this.height = height;
					
				}
				
				public double getValue( double finish ) {
					
					return Math.sin( finish - start )*height;
					
				}
				
			}
			
			public static class ShiningHintTask extends TimerTask {

				private static final double PROCEEDING_PERCENTAGE = 0.05;
				private static final int TIME_MILLIS_FOR_CHANGE = 60;
				
				private final SineWave calculator = new SineWave( Math.PI/2 , 0.5 );
				private int counter = 0;
				private JLabel shiningText;
				
				public ShiningHintTask( JLabel shiningText ) {
					
					this.shiningText = shiningText;
					
				}
				
				@Override
				public void run() {
					
					Color hintColor = shiningText.getForeground();
					double hintAlpha = hintColor.getAlpha();
					hintAlpha = 255*( calculator.getValue( Math.PI/2 + Math.PI*2*( counter/( TIME_MILLIS_FOR_CHANGE/PROCEEDING_PERCENTAGE ) ) ) + 0.5 );
					Color hintNewColor = new Color( hintColor.getRed() , hintColor.getGreen() , hintColor.getBlue() , (int)( hintAlpha ) );
					shiningText.setForeground( hintNewColor );
					
					counter += TIME_MILLIS_FOR_CHANGE;
					if ( counter >= TIME_MILLIS_FOR_CHANGE/PROCEEDING_PERCENTAGE )
						counter = 0;
					
				}
				
			}
			
			public static class ShiningHint extends Timer {
				
				public ShiningHint( JLabel text ) {
					
					schedule( new ShiningHintTask( text ) , 0 , ShiningHintTask.TIME_MILLIS_FOR_CHANGE );
					
				}
				
			}
			
		}
		
		public static class Bubbles extends JComponent {
			
			private final Bubbles self = this;
			
			private final Point containerCenter;
			
			public Bubbles( Container container ) {
				
				setSize( container.getWidth() , container.getHeight() );
				container.add( this );
				containerCenter = new Point( container.getWidth()/2 , container.getHeight()/2 );
				List<Point.Double> starterBubbleLocations = Arrays.asList( StarterTriangle.getVertexes() );
				starterBubbleLocations.forEach( new Consumer<Point.Double>() {

					@Override
					public void accept( Point.Double location ) {
						
						location.setLocation( location.x + containerCenter.x , location.x + containerCenter.y );
						new Bubble( location , self );
						
					}
					
				} );
				
			}
			
			public static class StarterTriangle {
				
				private static final double DISTANCE_FROM_CENTER = 100;
				private static final Point2D.Double center = new Point2D.Double();
				private static final Point2D.Double northVertex = new Point2D.Double( 0 , DISTANCE_FROM_CENTER*( -1 ) );
				private static final RegularTriangle result = new RegularTriangle( center , northVertex );				
				
				public static Point.Double[] getVertexes() {
					
					return result.getVertexes();
					
				}
				
				public static class RegularTriangle {
					
					private static final double SQUAREROOT_THREE = Math.sqrt( 3 );
					private static final double HALF_SQRT_THREE = SQUAREROOT_THREE/2;
					
					private Point2D.Double rightVertex = new Point2D.Double( 1 , 0 );
					private Point2D.Double leftTopVertex = new Point2D.Double( -0.5 , HALF_SQRT_THREE );
					private Point2D.Double leftBottomVertex = new Point2D.Double( -0.5 , HALF_SQRT_THREE*( -1 ) );
					private Point2D.Double[] unitVertexes = new Point2D.Double[] { rightVertex , leftTopVertex , leftBottomVertex };
					
					private Point2D.Double[] resultVertexes;

					public RegularTriangle( Point.Double center , Point.Double vertex ) {
						
						double lengthLikeRadius = distanceOf( center , vertex );
						Point2D.Double[] haventRotatedYet = multiply( unitVertexes , lengthLikeRadius );
						double angle = 0;
						if ( vertex.x > 0 && vertex.y > 0 )
							angle = Math.atan( vertex.y/vertex.x );
						else if ( vertex.x < 0 && vertex.y > 0 )
							angle = Math.atan( vertex.y/( vertex.x*( -1 ) ) ) + Math.PI/2;
						else if ( vertex.x < 0 && vertex.y < 0 )
							angle = Math.atan( ( vertex.y*( -1 ) )/( vertex.x*( -1 ) ) ) + Math.PI;
						else if ( vertex.x > 0 && vertex.y < 0 )
							angle = Math.atan( ( vertex.y*( -1 ) )/vertex.x ) + Math.PI*3/2;
						else if ( vertex.x > 0 && vertex.y == 0 )
							angle = 0;
						else if ( vertex.x == 0 && vertex.y > 0 )
							angle = Math.PI/2;
						else if ( vertex.x < 0 && vertex.y == 0 )
							angle = Math.PI;
						else if ( vertex.x == 0 && vertex.y < 0 )
							angle = Math.PI*3/2;
						Point2D.Double[] rotated = rotate( haventRotatedYet , angle );
						resultVertexes = rotated;
						
					}
					
					private double distanceOf( Point.Double p1 , Point.Double p2 ) {
						
						return Point.distance( p1.x , p1.y , p2.x , p2.y );
						
					}
					
					private Point2D.Double[] multiply( Point2D.Double[] original , double multiple ) {
						
						ArrayList<Point.Double> result = new ArrayList<Point.Double>();
						Arrays.asList( original ).forEach( new Consumer<Point.Double>() {

							@Override
							public void accept( Point.Double value ) {
								
								result.add( multiply( value , multiple ) );
								
							}
							
						} );
						return result.toArray( new Point.Double[] {} );
						
					}
					
					private Point.Double multiply( Point.Double original , double multiple ) {
						
						return new Point.Double( original.x*multiple , original.y*multiple );
						
					}
					
					private Point.Double[] rotate( Point.Double[] original , double angle ) {
						
						ArrayList<Point.Double> result = new ArrayList<Point.Double>();
						Arrays.asList( original ).forEach( new Consumer<Point.Double>() {

							@Override
							public void accept( Point.Double value ) {
								
								result.add( rotate( value , angle ) );
								
							}
							
						} );
						return result.toArray( new Point.Double[] {} );
						
					}
					
					private Point.Double rotate( Point.Double original , double angle ) {
						
						double oldX = original.x;
						double oldY = original.y;
						double cos = Math.cos( angle );
						double sin = Math.sin( angle );
						double newX = oldX*cos - oldY*sin;
						double newY = oldX*sin + oldY*cos;
						return new Point.Double( newX , newY );
						
					}
					
					public Point.Double[] getVertexes() {
						
						return resultVertexes;
						
					}
					
				}
				
			}
			
			public static class Bubble extends ImageCreator {
				
				public interface Appearance {
					
					default Color getEdgeColor() {
						
						return new Color( 119 , 255 , 51 ); // light green
						
					}
					
					default Color getCoreColor() {
						
						return new Color( 133 , 244 , 244 ); // light blue
						
					}
					
					default int getRadius() {
						
						return 20;
						
					}
					
					default int getEdgeBreadth() {
						
						return 5;
						
					}
					
					default Point getCenter() {
						
						return new Point( getRadius()/2 , getRadius()/2 );
						
					}
					
				}
				
				private static Appearance settings = new Appearance() {};
				private static HowToPaint bubbleShape = new HowToPaint() {
					
					public void draw( int x , int y , Graphics g ) {
						
						if ( Point.distance( (double)x , (double)y , settings.getCenter().x , settings.getCenter().y ) < settings.getRadius() ) {
							
							if ( Point.distance( (double)x , (double)y , settings.getCenter().x , settings.getCenter().y ) < settings.getRadius() - settings.getEdgeBreadth() )
								g.setColor( settings.getCoreColor() );
							else
								g.setColor( settings.getEdgeColor() );
							
							g.fillRect( x , y , 1 , 1 );
							
						}
						
					}
					
				};
				
				public Bubble( Point.Double center , Component toShow ) {
					
					super( toShow.getWidth() , toShow.getHeight() , null );
					//draw( toShow ,  );
					
//					int totalRadius = radius + strokeWidth;
//					int left = (int)( center.x - totalRadius );
//					int right = (int)( center.x + totalRadius );
//					int top = (int)( center.y - totalRadius );
//					int bottom = (int)( center.y + totalRadius );
//					x = left;
//					y = top;
//					setSize( right - left , bottom - top );
					
				}
				
//				@Override
//				public void paint( Graphics g ) {
//					super.paint( g );
//					
////					g.setColor( edge );
////					Dimension biggerSize = getSize();
////					g.fillOval( 0 , 0 , biggerSize.width , biggerSize.height );
////					g.setColor( background );
////					Dimension smallerSize = new Dimension( biggerSize.width - strokeWidth*2 , biggerSize.height - strokeWidth*2 );
////					g.fillOval( strokeWidth , strokeWidth , smallerSize.width , smallerSize.height );
////					setLocation( x , y );
//					
//					// test
//					g.setColor( edge );
//					Point center = getLocation();
//					center.translate( radius , radius );
//					Point.Double centerInDoubles = new Point.Double( (double)center.x , (double)center.y );
//					for ( CircleStructure.Pixel p : new CircleStructure( centerInDoubles , (double)radius, background ).getPixels() )
//						g.fillRect( p.x , p.y , 1 , 1 );
//					setLocation( x , y );
//					
//				}
				
				public static class CircleStructure {
					
					private List<Pixel> pixels = new ArrayList<Pixel>();
					private OuterBoundaryPixelDyer dyer = new OuterBoundaryPixelDyer();
					
					private Point.Double center;
					private double radius;
					private Color color;
					
					public CircleStructure( Point.Double center , double radius , Color color ) {
						
						pixels.add( new Pixel( (int)( center.x ) , (int)( center.y ) , color ) );
						this.center = center;
						this.radius = radius;
						this.color = color;
						Rectangle printRegion = new Rectangle( (int)( center.x - radius ) , (int)( center.y - radius ) , (int)( radius*2 ) , (int)( radius*2 ) );
						Rectangle tempRegion = new Rectangle();
						while ( printRegion.contains( getOuterEdge( ( tempRegion = dyer.getBoundary() ).width == 0 ? new Rectangle( (int)center.x , (int)center.y , 0 , 0 ) : tempRegion ) ) ) {
							
							pixels.addAll( dyer );
							enlarge( printRegion );
							
						}
						
					}
					
					private void enlarge( Rectangle boundary ) {
						
						dyer = new OuterBoundaryPixelDyer( pixels , boundary
								
							, new OuterBoundaryPixelDyer.PixelPrinter() {
									
								@Override
								public Color whatColorIs( int whereX , int whereY ) {
										
									return color;
										
								}
									
								@Override
								public boolean reallyInNeed( int whereX , int whereY ) {

									return Point.distance( center.x , center.y , (double)whereX , (double)whereY ) <= radius;
										
								}
								
						});
						
					}
					
					private Rectangle getOuterEdge( Rectangle r ) {
						
						return new Rectangle( r.x - 1 , r.y - 1 , r.width + 2 , r.height + 2 );
						
					}
					
					public List<Pixel> getPixels() {
						
						return pixels;
						
					}
					
					public static class Pixel {
						
						private int x;
						private int y;
						private Color color;
						
						public Pixel( int x , int y , Color color ) {
							
							this.x = x;
							this.y = y;
							this.color = color;
							
						}
						
						@Override
						public String toString() {
							
							return "<" + x + "," + y + "," + ColorUtility.code( color ) + ">";
							
						}
						
					}
					
					public static class OuterBoundaryPixelDyer extends ArrayList<Pixel> {
						
						public interface PixelPrinter {
							
							boolean reallyInNeed( int whereX , int whereY );
							Color whatColorIs( int whereX , int whereY );
							
						}
						
						private List<Pixel> chosen;
						private final PixelPrinter dye;
						
						public OuterBoundaryPixelDyer( List<Pixel> canvas , Rectangle pick , PixelPrinter dye ) {
							
							this.dye = dye;
							chosen = new Inclusion( canvas , pick );
							
							addSurroundings();
							PixelListUtility.removeLaterSamePlace( this );
							
						}
						public OuterBoundaryPixelDyer( List<Pixel> canvas , Rectangle pick , Color dye ) {
							
							this( canvas , pick , new PixelPrinter() {

								// Please ignore this method here.
								@Override
								public boolean reallyInNeed( int whereX, int whereY ) {
									return true;
								}

								@Override
								public Color whatColorIs( int whereX, int whereY ) {
									
									return dye;
									
								}
								
							} );
							
						}
						private OuterBoundaryPixelDyer() {
							
							this( new ArrayList<Pixel>() , new Rectangle() , new PixelPrinter() {

								// Please ignore this method here.
								@Override
								public boolean reallyInNeed( int whereX, int whereY ) {
									return false;
								}

								// Please ignore this method here.
								@Override
								public Color whatColorIs( int whereX, int whereY ) {
									
									return Color.WHITE;
									
								}
								
							} );
							
						}
						
						private void addSurroundings() {
							
							addStrangers( 0 , 1 );
							addStrangers( 0 , -1 );
							addStrangers( -1 , 0 );
							addStrangers( 1 , 0 );
							
						}
						
						// Reactangllllllllllllllllllllllllllle!!!
						
						private void addStrangers( int Xshift , int Yshift ) {
							
							for ( Pixel chosenOne : chosen )
								if ( new Exclusion( chosen , new Rectangle( chosenOne.x + Xshift , chosenOne.y + Yshift , 1 , 1 ) ).hasDoneAny() )
									continue;
								else {
									
									Point where = new Point( chosenOne.x + Xshift , chosenOne.y + Yshift );
									if ( dye.reallyInNeed( where.x , where.y ) )
										add( new Pixel( where.x , where.y , dye.whatColorIs( where.x , where.y ) ) );
									
								}
							
						}
						
						public Rectangle getBoundary() {
							
							if ( size() > 0 ) {
								
								Pixel firstPixel = get( 0 );
								Rectangle result = new Rectangle( firstPixel.x , firstPixel.y , 1 , 1 );
								for ( Pixel pixel : this )
									result = result.union( new Rectangle( pixel.x , pixel.y , 1 , 1 ) );
								return result;
								
							} else
								return new Rectangle();
							
						}
						
						public static class Inclusion extends ArrayList<Pixel> {
							
							public Inclusion( List<Pixel> pixels , Rectangle region ) {
								
								for ( Pixel pixel : pixels )
									if ( region.contains( pixel.x , pixel.y ) )
										add( pixel );
								
							}
							
						}
						
						public static class Exclusion extends ArrayList<Pixel> {
							
							private final int pixelCountStub;
							
							public Exclusion( List<Pixel> pixels , Rectangle region ) {
								
								pixelCountStub = pixels.size();
								addAll( pixels );
								for ( Pixel pixel : pixels )
									if ( region.contains( pixel.x , pixel.y ) )
										remove( pixel );
								
							}
							
							public boolean hasDoneAny() {
								
								return size() != pixelCountStub;
								
							}
							
						}
						
					}
					
				}
				
			}
			
		}
		
	}
	
	public static class AllTestsHere {
		
		private List<Boolean> areTestedSuccessful = new ArrayList<Boolean>();
		
		public AllTestsHere() {
			
			for ( Class<?> maybeTestClassOrInterfaceOrAnnotationClass : AllTestsHere.class.getDeclaredClasses() ) {
		
				Class<?> c = maybeTestClassOrInterfaceOrAnnotationClass;
				if ( ( c.isMemberClass() ) && ( isTestable( c ) ) ) {
					
					Class<?> testClass = c;
					try {
						
						Object test = testClass.getConstructor().newInstance();
						areTestedSuccessful.add( ((Test)test).isTrue() );
						
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
		
						e.printStackTrace();
						
					}
					
				}
				
			}
			
		}
		
		private boolean isTestable( Class<?> c ) {
			
			return c.getAnnotation( ForTest.class ) != null;
			
		}
		
		public boolean areAllTestsPassed() {
			
			if ( areTestedSuccessful.size() == 0 )
				return false;
			for ( boolean isTestedSuccessful : areTestedSuccessful )
				if ( ! isTestedSuccessful )
					return false;
			return true;
			
		}
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.TYPE)
		public @interface ForTest{}
		
		public interface Test {
			
			boolean isTrue();
			
		}
		
		public static class FilesEquality {
			
			public static boolean really( File fileA , File fileB ) {
				
				try {
					List<Boolean> fileAbooleans = new FileBytes( Files.readAllBytes( new File( "C://Users/lucky/Desktop/aha/a.txt" ).toPath() ) ).toBooleans();
					List<Boolean> fileBbooleans = new FileBytes( Files.readAllBytes( new File( "C://Users/lucky/Desktop/aha/b.txt" ).toPath() ) ).toBooleans();
					if ( fileAbooleans.size() == fileBbooleans.size() ) {
						
						for ( int i = 0 ; i < fileAbooleans.size() ; i++ )
							if ( fileAbooleans.get( i ) != fileBbooleans.get( i ) )
								return false;
						return true;
						
					} else
						return false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
				
			}
			
		}
		
		@ForTest
		public static class FilesEqualityTest implements Test {
			
			@Override
			public boolean isTrue() {
				
				return FilesEquality.really( new File( "C://Users/lucky/Desktop/aha/a.txt" ) , new File( "C://Users/lucky/Desktop/aha/b.txt" ) );
				
			}
			
		}
		
		@ForTest
		public static class BinaryNumberTest implements Test {
			
			@Override
			public boolean isTrue() {
				
				boolean test1 = new BinaryNumber( 10 ).toString().equals( "[true, false, true, false]" );
				boolean test2 = new BinaryNumber( 10 , 6 ).toString().equals( "[false, false, true, false, true, false]" );
				return test1 && test2;
				
			}
			
		}
		
		@ForTest
		public static class BinaryNumberWithItsSizeTest implements Test {

			@Override
			public boolean isTrue() {
				
				boolean test1 = new BinaryNumberWithItsSize( 10 ).getSize() == 4;
				boolean test2 = new BinaryNumberWithItsSize( 10 , 6 ).getSize() == 6;
				return test1 && test2;
				
			}
			
		}
		
		@ForTest
		public static class BinaryNumberWithMultipleIndexTest implements Test {

			private BinaryNumberWithMultipleIndex test1 = new BinaryNumberWithMultipleIndex( 16 );
			private BinaryNumberWithMultipleIndex test2 = new BinaryNumberWithMultipleIndex( 32 );
			private BinaryNumberWithMultipleIndex test3 = new BinaryNumberWithMultipleIndex( 16 , 7 );
			private BinaryNumberWithMultipleIndex test4 = new BinaryNumberWithMultipleIndex( 32 , 7 );
			
			@Override
			public boolean isTrue() {

				test1.whenMeetUpMultiples( 3 , new Consumer<Integer>() {

					@Override
					public void accept( Integer i ) {
						
						test1.add( i , true );
						
					}
					
				} );
				test2.whenMeetUpMultiples( 3 , new Consumer<Integer>() {

					@Override
					public void accept( Integer i ) {
						
						test2.add( i , true );
						
					}
					
				} );
				test3.whenMeetUpMultiples( 3 , new Consumer<Integer>() {

					@Override
					public void accept( Integer i ) {
						
						test3.add( i , true );
						
					}
					
				});
				test4.whenMeetUpMultiples( 3 , new Consumer<Integer>() {

					@Override
					public void accept( Integer i ) {
						
						test4.add( i , true );
						
					}
					
				});
				boolean test1Result = test1.toString().equals( "[true, true, false, true, false, false, true, false]" );
				boolean test2Result = test2.toString().equals( "[true, true, false, true, false, false, true, false, false]" );
				boolean test3Result = test3.toString().equals( "[true, false, false, true, true, false, true, false, false, true, false]" );
				boolean test4Result = test4.toString().equals( "[true, false, true, true, false, false, true, false, false, true, false]" );
				return test1Result && test2Result && test3Result && test4Result;
				
			}
			
		}
		
		@ForTest
		public static class BinaryNumberWithMultipleTrueIndexTest implements Test {

			@Override
			public boolean isTrue() {
				
				BinaryNumberWithMultipleTrueIndex test1 = new BinaryNumberWithMultipleTrueIndex( 10 , 3 );
				BinaryNumberWithMultipleTrueIndex test2 = new BinaryNumberWithMultipleTrueIndex( 10 , 3 , 6 );
				boolean test1Result = test1.toString().equals( "[true, true, false, true, true, false]" ) && ( test1.getMultiple() == 3 );
				boolean test2Result = ( test2.toString().equals( "[true, false, false, true, true, false, true, true, false]" ) ) && ( test2.getMultiple() == 3 );
				return test1Result && test2Result;
				
			}
			
		}
		
		@ForTest
		public static class SerializableNumberTest implements Test {

			private SerializableNumber test1 = new SerializableNumber( 4 , 2 );
			private SerializableNumber test2 = new SerializableNumber( 4 , 2 , 6 );
			
			@Override
			public boolean isTrue() {
				
				boolean test1Result = test1.toString().equals( "[true, true, true, false, false, false]" );
				boolean test2Result = test2.toString().equals( "[true, false, true, false, true, false, true, true, true, false, false, false]" );
				return test1Result && test2Result;
				
			}
			
		}
		
		@ForTest
		public static class SerializableEachBitNumberTest implements Test {

			@Override
			public boolean isTrue() {
				
				return new SerializableEachBitNumber( 10 ).toString().equals( "[true, true, true, false, true, true, false, false]" );
				
			}
			
		}
		
		@ForTest
		public static class SerializableByteNumberTest implements Test {

			@Override
			public boolean isTrue() {
				
				// Result descriptions don't include their formats.
				boolean fullBitsResult = new SerializableByteNumber( 16383 ).toString().equals( "[true, true, true, true, true, true, true, true, false, true, true, true, true, true, true, true]" );
				boolean firstBitEmptyResult = new SerializableByteNumber( 8191 ).toString().equals( "[true, false, true, true, true, true, true, true, false, true, true, true, true, true, true, true]" );
				return fullBitsResult && firstBitEmptyResult;
				
			}
			
		}
		
		@ForTest
		public static class SerializableNumberReaderTest implements Test {

			@Override
			public boolean isTrue() {
				
				boolean singleNumberTest = SerializableNumberReader.read( new SerializableEachBitNumber( 100 ) , 2 ) == 100;
				boolean multipleNumbersButOneTest = SerializableNumberReader.read( new SerializableEachBitNumber( 100 ) , 2 , 1 ).get( 0 ) == 100;
				
				List<Boolean> eachBitsBinaryNumbersSample = new SerializableEachBitNumber( 100 ) {
					
					{
						
						addAll( new SerializableEachBitNumber( 200 ) );
						addAll( new SerializableEachBitNumber( 300 ) );
						
					}
					
				};
				boolean multipleNumbersNormalTest = SerializableNumberReader.read( eachBitsBinaryNumbersSample , 2 , 2 ).toString().equals( "[100, 200]" );
				List<Boolean> byteBinaryNumbersSample = new SerializableByteNumber( 100000 ) {
					
					{
						
						addAll( new SerializableByteNumber( 200000 ) );
						addAll( new SerializableByteNumber( 300000 ) );
						
					}
					
				};
				boolean multipleNumbersEndingTest = SerializableNumberReader.read( byteBinaryNumbersSample , 8 , 3 ).toString().equals( "[100000, 200000, 300000]" );
				return singleNumberTest && multipleNumbersButOneTest && multipleNumbersNormalTest && multipleNumbersEndingTest;
				
			}
			
		}
		
		@ForTest
		public static class SerializableFormatReaderTest implements Test {

			@Override
			public boolean isTrue() {
				
				return SerializableFormatReader.getLengthes( Arrays.asList( new Boolean[] { false , true , true , false , true , false , true , true } ) ).toString().equals( "[1, 3, 2]" );
				
			}
			
		}
		
		@ForTest
		public static class BinaryCalculatorTest implements Test {
			
			@Override
			public boolean isTrue() {
				
				return BinaryCalculator.toInteger( Arrays.asList( new Boolean[] { true , true , false } ) ) == 6;
				
			}
			
		}
		
		@ForTest
		public static class BinaryDigitsPrinterTest implements Test {

			@Override
			public boolean isTrue() {
				
				return new BinaryDigitsPrinter( Arrays.asList( new Boolean[] { true , false , true } ) ).toString().equals( "101" );
				
			}
			
		}
		
		@ForTest
		public static class MathUtilityTest implements Test {

			@Override
			public boolean isTrue() {
			
				boolean test1Result = ( MathUtility.findHigherOrEquallyMultiple( 10 , 3 ) == 12 );
				boolean test2Result = ( MathUtility.findHigherOrEquallyMultiple( 12 , 3 ) == 12 );
				return test1Result && test2Result;
				
			}	
			
		}
		
		@ForTest
		public static class IntegerListUtilityTest implements Test {

			@Override
			public boolean isTrue() {

				return IntegerListUtility.sumUpOneByOneAndGraduallyIncrease( Arrays.asList( new Integer[] { 1 , 2 , 3 } ) ).toString().equals( "[1, 3, 6]" );
				
			}
			
		}
		
		@ForTest
		public static class ColorUtilityTest implements Test {

			@Override
			public boolean isTrue() {

				boolean test1 = ColorUtility.code( Color.GRAY ).equals( "(128,128,128,255)" );
				return test1;
				
			}
			
		}
		
		@ForTest
		public static class PixelListUtilityTest implements Test {

			private AIframe.Bubbles.Bubble.CircleStructure.Pixel p1 = new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 10 , 10 , Color.GRAY );
			private AIframe.Bubbles.Bubble.CircleStructure.Pixel p2 = new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 10 , 10 , Color.PINK );
			private AIframe.Bubbles.Bubble.CircleStructure.Pixel p3 = new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 10 , 15 , Color.ORANGE );
			private List<AIframe.Bubbles.Bubble.CircleStructure.Pixel> matrix = new ArrayList<AIframe.Bubbles.Bubble.CircleStructure.Pixel>() {
				
				{
					
					add( p1 );
					add( p2 );
					add( p3 );
					
				}
				
			};
			
			@Override
			public boolean isTrue() {

				boolean testForHasSamePlace = PixelListUtility.hasSamePlace( p1 , p2 );
				boolean testForSamePlaceOccurs = PixelListUtility.samePlaceOccurs( p2 , matrix );
				boolean testForRemoveLaterSamePlace = PixelListUtility.removeLaterSamePlace( matrix ).toString().equals( "[<10,10,(128,128,128,255)>, <10,15,(255,200,0,255)>]" );
				return testForHasSamePlace && testForSamePlaceOccurs && testForRemoveLaterSamePlace;
				
			}
			
		}
		
		@ForTest
		public static class InclusionTest implements Test {

			ArrayList<AIframe.Bubbles.Bubble.CircleStructure.Pixel> matrix = new ArrayList<AIframe.Bubbles.Bubble.CircleStructure.Pixel>() {
				
				{
					
					add( new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 20 , 10 , Color.GRAY ) );
					add( new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 26 , 32 , Color.GRAY ) );
					add( new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 28 , 33 , Color.GRAY ) );
					
				}
				
			};
			
			@Override
			public boolean isTrue() {
				
				return new AIframe.Bubbles.Bubble.CircleStructure.OuterBoundaryPixelDyer.Inclusion( matrix , new Rectangle( 25 , 25 , 10 , 10 ) ).toString().equals( "[<26,32,(128,128,128,255)>, <28,33,(128,128,128,255)>]" );
				
			}
			
		}
		
		@ForTest
		public static class ExclusionTest implements Test {

			ArrayList<AIframe.Bubbles.Bubble.CircleStructure.Pixel> matrix = new ArrayList<AIframe.Bubbles.Bubble.CircleStructure.Pixel>() {
				
				{
					
					add( new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 20 , 10 , Color.GRAY ) );
					add( new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 26 , 32 , Color.GRAY ) );
					add( new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 28 , 33 , Color.GRAY ) );
					
				}
				
			};
			
			@Override
			public boolean isTrue() {

				AIframe.Bubbles.Bubble.CircleStructure.OuterBoundaryPixelDyer.Exclusion test = new AIframe.Bubbles.Bubble.CircleStructure.OuterBoundaryPixelDyer.Exclusion( matrix , new Rectangle( 25 , 25 , 10 , 10 ) );
				boolean testForConstructor = test.toString().equals( "[<20,10,(128,128,128,255)>]" );
				boolean testForHasDoneAny = test.hasDoneAny();
				return testForConstructor && testForHasDoneAny;
				
			}
			
		}
		
		@ForTest
		public static class OuterBoundaryPixelDyerTest implements Test {

				ArrayList<AIframe.Bubbles.Bubble.CircleStructure.Pixel> matrix = new ArrayList<AIframe.Bubbles.Bubble.CircleStructure.Pixel>() {
				
				{
					
					add( new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 20 , 10 , Color.GRAY ) );
					add( new AIframe.Bubbles.Bubble.CircleStructure.Pixel( 20 , 11 , Color.GRAY ) );
					
				}
				
			};
			
			@Override
			public boolean isTrue() {

				AIframe.Bubbles.Bubble.CircleStructure.OuterBoundaryPixelDyer testParent = new AIframe.Bubbles.Bubble.CircleStructure.OuterBoundaryPixelDyer( matrix , new Rectangle( 5 , 5 , 25 , 25 ) , Color.RED );
				boolean testSizeForConstructor = testParent.size() == 6;
				boolean testForConstructor = testParent.toString().equals( "[<20,12,(255,0,0,255)>, <20,9,(255,0,0,255)>, <19,10,(255,0,0,255)>, <19,11,(255,0,0,255)>, <21,10,(255,0,0,255)>, <21,11,(255,0,0,255)>]" );
				boolean testSummaryForConstructor = testSizeForConstructor && testForConstructor;
				testParent.addSurroundings();
				boolean testSizeForAddSurroundings = testParent.size() == 12;
				boolean testForAddSurroundings = testParent.toString().equals( "[<20,12,(255,0,0,255)>, <20,9,(255,0,0,255)>, <19,10,(255,0,0,255)>, <19,11,(255,0,0,255)>, <21,10,(255,0,0,255)>, <21,11,(255,0,0,255)>, <20,12,(255,0,0,255)>, <20,9,(255,0,0,255)>, <19,10,(255,0,0,255)>, <19,11,(255,0,0,255)>, <21,10,(255,0,0,255)>, <21,11,(255,0,0,255)>]" );
				boolean testSummaryForAddSurroundings = testSizeForAddSurroundings && testForAddSurroundings;
				testParent.addStrangers( 0 , 1 );
				boolean testSizeForAddStrangers = testParent.size() == 13;
				boolean testForAddStrangers = testParent.toString().equals( "[<20,12,(255,0,0,255)>, <20,9,(255,0,0,255)>, <19,10,(255,0,0,255)>, <19,11,(255,0,0,255)>, <21,10,(255,0,0,255)>, <21,11,(255,0,0,255)>, <20,12,(255,0,0,255)>, <20,9,(255,0,0,255)>, <19,10,(255,0,0,255)>, <19,11,(255,0,0,255)>, <21,10,(255,0,0,255)>, <21,11,(255,0,0,255)>, <20,12,(255,0,0,255)>]" );
				boolean testSummaryForAddStrangers = testSizeForAddStrangers && testForAddStrangers;
				AIframe.Bubbles.Bubble.CircleStructure.OuterBoundaryPixelDyer anotherTestParent = new AIframe.Bubbles.Bubble.CircleStructure.OuterBoundaryPixelDyer( matrix , new Rectangle( 5 , 5 , 25 , 25 ) ,
						
					new AIframe.Bubbles.Bubble.CircleStructure.OuterBoundaryPixelDyer.PixelPrinter() {
						
						@Override
						public Color whatColorIs( int whereX, int whereY ) {

							return Color.GREEN;
							
						}
						
						@Override
						public boolean reallyInNeed( int whereX, int whereY ) {
							
							return whereX == 20 && whereY == 12;
							
						}
						
					}
						
				);
				boolean testSizeForPixelPrinter = anotherTestParent.size() == 1;
				boolean testForPixelPrinter = anotherTestParent.toString().equals( "[<20,12,(0,255,0,255)>]" );
				boolean testSummaryForPixelPrinter = testSizeForPixelPrinter && testForPixelPrinter;
				
				boolean testForGetBoundary = testParent.getBoundary().toString().equals( "java.awt.Rectangle[x=19,y=9,width=3,height=4]" );

				return testSummaryForConstructor && testSummaryForAddSurroundings && testSummaryForAddStrangers && testSummaryForPixelPrinter && testForGetBoundary;
				
			}
			
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//HappyZip.zip( new File( "C://Users/felix/Desktop/Felix Lin Java library/a.txt" ) );
		
		System.out.println( new AllTestsHere().areAllTestsPassed() );
		
		//File test = FileShell.in( new File( "C://Users/yaya/Desktop/Happy Zip/test/a.txt" ) );
		//FileShell.out( test );
		
		AIframe ai = new AIframe();
		Frame stage = ai.getCanvas();
		BufferedImage testBlock = ai.getImage();
		System.out.println( testBlock );
		for ( int x = 0 ; x < 100 ; x++ )
			for ( int y = 0 ; y < 100 ; y++ )
				testBlock.setRGB( x , y , Color.BLUE.getRGB() );
		stage.paintAll( testBlock.getGraphics() );
		//stage.getGraphics().drawImage(testBlock, 0, 0, stage); //(not working)
		/*stage.add( new Component() {

			{
				
				setSize( testBlock.getWidth() , testBlock.getHeight() );
				
			}
			
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage( testBlock , 0 , 0 , null );
			}
			
		} );*/ //(It doesn't work)
		
		stage.repaint();
		
		//
		//System.out.println( Arrays.asList( new AIframe.Bubbles.StarterTriangle.RegularTriangle( new Point.Double( 0 , 0 ) , new Point.Double( 0 , -100 ) ).getVertexes() ) );
	}

}
