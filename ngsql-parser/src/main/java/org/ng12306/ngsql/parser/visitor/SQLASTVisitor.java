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
package org.ng12306.ngsql.parser.visitor;

import org.ng12306.ngsql.parser.ast.expression.BinaryOperatorExpression;
import org.ng12306.ngsql.parser.ast.expression.PolyadicOperatorExpression;
import org.ng12306.ngsql.parser.ast.expression.UnaryOperatorExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.BetweenAndExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.ComparisionEqualsExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.ComparisionIsExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.ComparisionNullSafeEqualsExpression;
import org.ng12306.ngsql.parser.ast.expression.comparison.InExpression;
import org.ng12306.ngsql.parser.ast.expression.logical.LogicalAndExpression;
import org.ng12306.ngsql.parser.ast.expression.logical.LogicalOrExpression;
import org.ng12306.ngsql.parser.ast.expression.misc.InExpressionList;
import org.ng12306.ngsql.parser.ast.expression.misc.UserExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.CaseWhenOperatorExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.DefaultValue;
import org.ng12306.ngsql.parser.ast.expression.primary.ExistsPrimary;
import org.ng12306.ngsql.parser.ast.expression.primary.Identifier;
import org.ng12306.ngsql.parser.ast.expression.primary.MatchExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.ParamMarker;
import org.ng12306.ngsql.parser.ast.expression.primary.PlaceHolder;
import org.ng12306.ngsql.parser.ast.expression.primary.RowExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.SysVarPrimary;
import org.ng12306.ngsql.parser.ast.expression.primary.UsrDefVarPrimary;
import org.ng12306.ngsql.parser.ast.expression.primary.function.FunctionExpression;
import org.ng12306.ngsql.parser.ast.expression.primary.function.cast.Cast;
import org.ng12306.ngsql.parser.ast.expression.primary.function.cast.Convert;
import org.ng12306.ngsql.parser.ast.expression.primary.function.datetime.Extract;
import org.ng12306.ngsql.parser.ast.expression.primary.function.datetime.GetFormat;
import org.ng12306.ngsql.parser.ast.expression.primary.function.datetime.Timestampadd;
import org.ng12306.ngsql.parser.ast.expression.primary.function.datetime.Timestampdiff;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Avg;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Count;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.GroupConcat;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Max;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Min;
import org.ng12306.ngsql.parser.ast.expression.primary.function.groupby.Sum;
import org.ng12306.ngsql.parser.ast.expression.primary.function.string.Char;
import org.ng12306.ngsql.parser.ast.expression.primary.function.string.Trim;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.IntervalPrimary;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralBitField;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralBoolean;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralHexadecimal;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralNull;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralNumber;
import org.ng12306.ngsql.parser.ast.expression.primary.literal.LiteralString;
import org.ng12306.ngsql.parser.ast.expression.string.LikeExpression;
import org.ng12306.ngsql.parser.ast.expression.type.CollateExpression;
import org.ng12306.ngsql.parser.ast.fragment.GroupBy;
import org.ng12306.ngsql.parser.ast.fragment.Limit;
import org.ng12306.ngsql.parser.ast.fragment.OrderBy;
import org.ng12306.ngsql.parser.ast.fragment.ddl.ColumnDefinition;
import org.ng12306.ngsql.parser.ast.fragment.ddl.TableOptions;
import org.ng12306.ngsql.parser.ast.fragment.ddl.datatype.DataType;
import org.ng12306.ngsql.parser.ast.fragment.ddl.index.IndexColumnName;
import org.ng12306.ngsql.parser.ast.fragment.ddl.index.IndexOption;
import org.ng12306.ngsql.parser.ast.fragment.tableref.Dual;
import org.ng12306.ngsql.parser.ast.fragment.tableref.IndexHint;
import org.ng12306.ngsql.parser.ast.fragment.tableref.InnerJoin;
import org.ng12306.ngsql.parser.ast.fragment.tableref.NaturalJoin;
import org.ng12306.ngsql.parser.ast.fragment.tableref.OuterJoin;
import org.ng12306.ngsql.parser.ast.fragment.tableref.StraightJoin;
import org.ng12306.ngsql.parser.ast.fragment.tableref.SubqueryFactor;
import org.ng12306.ngsql.parser.ast.fragment.tableref.TableRefFactor;
import org.ng12306.ngsql.parser.ast.fragment.tableref.TableReferences;
import org.ng12306.ngsql.parser.ast.stmt.dal.DALSetCharacterSetStatement;
import org.ng12306.ngsql.parser.ast.stmt.dal.DALSetNamesStatement;
import org.ng12306.ngsql.parser.ast.stmt.dal.DALSetStatement;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowAuthors;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowBinLogEvent;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowBinaryLog;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowCharaterSet;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowCollation;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowColumns;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowContributors;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowCreate;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowDatabases;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowEngine;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowEngines;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowErrors;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowEvents;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowFunctionCode;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowFunctionStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowGrants;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowIndex;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowMasterStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowOpenTables;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowPlugins;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowPrivileges;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProcedureCode;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProcedureStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProcesslist;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProfile;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowProfiles;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowSlaveHosts;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowSlaveStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowTableStatus;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowTables;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowTriggers;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowVariables;
import org.ng12306.ngsql.parser.ast.stmt.dal.ShowWarnings;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLAlterTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLAlterTableStatement.AlterSpecification;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLCreateIndexStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLCreateTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLDropIndexStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLDropTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLRenameTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DDLTruncateStatement;
import org.ng12306.ngsql.parser.ast.stmt.ddl.DescTableStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLCallStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLDeleteStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLInsertStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLReplaceStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLSelectStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLSelectUnionStatement;
import org.ng12306.ngsql.parser.ast.stmt.dml.DMLUpdateStatement;
import org.ng12306.ngsql.parser.ast.stmt.extension.ExtDDLCreatePolicy;
import org.ng12306.ngsql.parser.ast.stmt.extension.ExtDDLDropPolicy;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSReleaseStatement;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSRollbackStatement;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSSavepointStatement;
import org.ng12306.ngsql.parser.ast.stmt.mts.MTSSetTransactionStatement;

/**
 * @author <a href="mailto:wenjie.0617@gmail.com">wuwj</a>
 */
public interface SQLASTVisitor {

	void visit(BetweenAndExpression node);

    void visit(ComparisionIsExpression node);

    void visit(InExpressionList node);

    void visit(LikeExpression node);

    void visit(CollateExpression node);

    void visit(UserExpression node);

    void visit(UnaryOperatorExpression node);

    void visit(BinaryOperatorExpression node);

    void visit(PolyadicOperatorExpression node);

    void visit(LogicalAndExpression node);

    void visit(LogicalOrExpression node);

    void visit(ComparisionEqualsExpression node);

    void visit(ComparisionNullSafeEqualsExpression node);

    void visit(InExpression node);

    //-------------------------------------------------------
    void visit(FunctionExpression node);

    void visit(Char node);

    void visit(Convert node);

    void visit(Trim node);

    void visit(Cast node);

    void visit(Avg node);

    void visit(Max node);

    void visit(Min node);

    void visit(Sum node);

    void visit(Count node);

    void visit(GroupConcat node);

    void visit(Extract node);

    void visit(Timestampdiff node);

    void visit(Timestampadd node);

    void visit(GetFormat node);

    //-------------------------------------------------------
    void visit(IntervalPrimary node);

    void visit(LiteralBitField node);

    void visit(LiteralBoolean node);

    void visit(LiteralHexadecimal node);

    void visit(LiteralNull node);

    void visit(LiteralNumber node);

    void visit(LiteralString node);

    void visit(CaseWhenOperatorExpression node);

    void visit(DefaultValue node);

    void visit(ExistsPrimary node);

    void visit(PlaceHolder node);

    void visit(Identifier node);

    void visit(MatchExpression node);

    void visit(ParamMarker node);

    void visit(RowExpression node);

    void visit(SysVarPrimary node);

    void visit(UsrDefVarPrimary node);

    //-------------------------------------------------------
    void visit(IndexHint node);

    void visit(InnerJoin node);

    void visit(NaturalJoin node);

    void visit(OuterJoin node);

    void visit(StraightJoin node);

    void visit(SubqueryFactor node);

    void visit(TableReferences node);

    void visit(TableRefFactor node);

    void visit(Dual dual);

    void visit(GroupBy node);

    void visit(Limit node);

    void visit(OrderBy node);

    void visit(ColumnDefinition node);

    void visit(IndexOption node);

    void visit(IndexColumnName node);

    void visit(TableOptions node);

    void visit(AlterSpecification node);

    void visit(DataType node);

    //-------------------------------------------------------
    void visit(ShowAuthors node);

    void visit(ShowBinaryLog node);

    void visit(ShowBinLogEvent node);

    void visit(ShowCharaterSet node);

    void visit(ShowCollation node);

    void visit(ShowColumns node);

    void visit(ShowContributors node);

    void visit(ShowCreate node);

    void visit(ShowDatabases node);

    void visit(ShowEngine node);

    void visit(ShowEngines node);

    void visit(ShowErrors node);

    void visit(ShowEvents node);

    void visit(ShowFunctionCode node);

    void visit(ShowFunctionStatus node);

    void visit(ShowGrants node);

    void visit(ShowIndex node);

    void visit(ShowMasterStatus node);

    void visit(ShowOpenTables node);

    void visit(ShowPlugins node);

    void visit(ShowPrivileges node);

    void visit(ShowProcedureCode node);

    void visit(ShowProcedureStatus node);

    void visit(ShowProcesslist node);

    void visit(ShowProfile node);

    void visit(ShowProfiles node);

    void visit(ShowSlaveHosts node);

    void visit(ShowSlaveStatus node);

    void visit(ShowStatus node);

    void visit(ShowTables node);

    void visit(ShowTableStatus node);

    void visit(ShowTriggers node);

    void visit(ShowVariables node);

    void visit(ShowWarnings node);

    void visit(DescTableStatement node);

    void visit(DALSetStatement node);

    void visit(DALSetNamesStatement node);

    void visit(DALSetCharacterSetStatement node);

    //-------------------------------------------------------
    void visit(DMLCallStatement node);

    void visit(DMLDeleteStatement node);

    void visit(DMLInsertStatement node);

    void visit(DMLReplaceStatement node);

    void visit(DMLSelectStatement node);

    void visit(DMLSelectUnionStatement node);

    void visit(DMLUpdateStatement node);

    void visit(MTSSetTransactionStatement node);

    void visit(MTSSavepointStatement node);

    void visit(MTSReleaseStatement node);

    void visit(MTSRollbackStatement node);

    void visit(DDLTruncateStatement node);

    void visit(DDLAlterTableStatement node);

    void visit(DDLCreateIndexStatement node);

    void visit(DDLCreateTableStatement node);

    void visit(DDLRenameTableStatement node);

    void visit(DDLDropIndexStatement node);

    void visit(DDLDropTableStatement node);

    void visit(ExtDDLCreatePolicy node);

    void visit(ExtDDLDropPolicy node);
}
