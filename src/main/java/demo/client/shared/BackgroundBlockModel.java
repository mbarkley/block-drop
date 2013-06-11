package demo.client.shared;

import java.util.ArrayList;
import java.util.List;

public class BackgroundBlockModel extends BlockModel {

	private List<Integer[]> offsets;
	
	public BackgroundBlockModel() {
		super(generateId());
		
		offsets = new ArrayList<Integer[]>();
	}
	
	public void addOffset(Integer[] offset) {
		offsets.add(offset);
	}
}
