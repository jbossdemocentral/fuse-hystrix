
package com.redhat.examples.definition;

import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class DefinitionService {

	private HashMap<String,String> dictionaryMap =new HashMap<String,String>();
	
	@PostConstruct
	public void initIt() throws Exception {
	  dictionaryMap.put("camel", "Either of two large, humped, ruminant quadrupeds of the genus");
	}
	
	public Result lookup(String word) {
		
		Result result = new Result();
		result.setInput(word);
		
		result.setDefinition(dictionaryMap.get(word));
		
		return result;
		
	}
	
}
