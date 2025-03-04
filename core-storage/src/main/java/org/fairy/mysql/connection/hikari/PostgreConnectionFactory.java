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

package org.fairy.mysql.connection.hikari;

import org.fairy.RepositoryType;
import org.fairy.mysql.pojo.statement.PostgresStatementBuilder;
import org.fairy.mysql.pojo.statement.SqlStatementBuilder;

public class PostgreConnectionFactory extends HikariConnectionFactory {
    @Override
    public String defaultPort() {
        return "5432";
    }

    @Override
    public void configureDatabase(String address, String port, String databaseName, String username, String password) {
        this.config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        this.config.addDataSourceProperty("serverName", address);
        this.config.addDataSourceProperty("portNumber", port);
        this.config.addDataSourceProperty("databaseName", databaseName);
        this.config.addDataSourceProperty("user", username);
        this.config.addDataSourceProperty("password", password);
    }

    @Override
    public RepositoryType type() {
        return RepositoryType.POSTGRE;
    }

    @Override
    public SqlStatementBuilder builder() {
        return new PostgresStatementBuilder();
    }

}