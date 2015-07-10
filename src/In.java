import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

class In{
	DataInputStream in;
	public In(String fileName) throws Exception{
		in= new DataInputStream(new FileInputStream(new File(fileName)));
	}
	
	public int readInt() throws Exception{
		String s="";
		
		Character ch= (char)in.read();
		while(!Character.isDigit(ch))ch= (char)in.read();
		while(Character.isDigit(ch))
		{
			s+=ch;
			ch=(char)in.read();
		}
		return Integer.parseInt(s);
	}
}