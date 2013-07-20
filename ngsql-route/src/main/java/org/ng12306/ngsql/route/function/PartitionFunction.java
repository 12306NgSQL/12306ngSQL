/*
 * Copyright 2012-2013 NgSql Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ng12306.ngsql.route.function;

import java.util.List;

import org.ng12306.ngsql.parser.ast.expression.Expression;
import org.ng12306.ngsql.parser.ast.expression.primary.function.FunctionExpression;
import org.ng12306.ngsql.route.util.PartitionUtil;

/*-
 * 数据库拆分抽象类，对应rule.xml的function字段
 * @author:Fredric
 * @date: 2013-5-17
 */
public abstract class PartitionFunction extends FunctionExpression {
	
	protected PartitionUtil partitionUtil;
	   
	   public PartitionFunction(String functionName, List<Expression> arguments) {
		super(functionName, arguments);
		// TODO Auto-generated constructor stub
	}

	   protected int count; //rule.xml的partitionCount字段
	   protected int length;//rule.xml的partitionLength字段
	   
	    public void setPartitionCount(String partitionCount) {
	        this.count = Integer.parseInt(partitionCount);
	    }

	    public void setPartitionLength(String partitionLength) {
	        this.length = Integer.parseInt(partitionLength);
	    }
	    
	    @Override
	    public void init() {
	        partitionUtil = new PartitionUtil(count, length);
	    }

	    protected int partitionIndex(long hash) {
	        return partitionUtil.partition(hash);
	    }
}
