/*
 * MIT License
 *
 * Copyright (c) 2021 Imanity
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.fairy.mysql.pojo.statement;

import org.fairy.mysql.pojo.Query;
import org.fairy.mysql.pojo.info.PojoInfo;

public interface SqlStatementBuilder {
	
	public String getInsertSql(Query query, Object row);
	public Object[] getInsertArgs(Query query, Object row);

	public String getUpdateSql(Query query, Object row);
	public Object[] getUpdateArgs(Query query, Object row);
	
	public String getDeleteSql(Query query, Object row);
	public Object[] getDeleteArgs(Query query, Object row);

	public String getUpsertSql(Query query, Object row);
	public Object[] getUpsertArgs(Query query, Object row);
	
	public String getSelectSql(Query query, Class<?> rowClass);
	public String getCreateTableSql(Class<?> clazz);
	
	public PojoInfo getPojoInfo(Class<?> rowClass);

	public Object convertValue(Object value, String columnTypeName);

}
