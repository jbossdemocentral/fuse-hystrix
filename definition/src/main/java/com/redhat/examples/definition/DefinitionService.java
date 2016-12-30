
package com.redhat.examples.definition;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class DefinitionService {

	private volatile HashMap<String,String> dictionaryMap =new HashMap<String,String>();
	
	@PostConstruct
	public void initIt() throws Exception {
	  dictionaryMap.put("camel", "Either of two large, humped, ruminant quadrupeds of the genus");
	  dictionaryMap.put("reprieve", "Postponement of a sentence");
	  dictionaryMap.put("common", "Occurring, found, or done often");
	  dictionaryMap.put("already", "Prior to or at some specified or implied time");
	  dictionaryMap.put("direct", "To guide, tell, or show (a person) the way to a place");
	  dictionaryMap.put("establish", "To install or settle in a position, place, business, etc");
	  dictionaryMap.put("finance", "The management of revenues");
	  dictionaryMap.put("locate", "To identify or discover the place or location of");
	  dictionaryMap.put("occupy", "To take or fill up");
	  dictionaryMap.put("poll", "To take a sampling of the attitudes or opinions of");

	}
	
	public Result lookup(String word) {
		
		Result result = new Result();
		result.setInput(word);
		
		result.setDefinition(dictionaryMap.get(word.toLowerCase()));
		
		return result;
		
	}
	
}
