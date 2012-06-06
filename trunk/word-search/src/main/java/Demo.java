import com.tfc.analysis.KWSeeker;
import com.tfc.analysis.entity.Keyword;

public class Demo {
	public static void main(String[] args) {
		KWSeeker kw1 = KWSeeker.getInstance(new Keyword("test1"), new Keyword("tes2"));
		// 添加一个词
		kw1.addWord(new Keyword("test3"));
	}
}
