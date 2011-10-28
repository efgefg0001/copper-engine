/*
 * Copyright 2002-2011 SCOOP Software GmbH
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
package de.scoopgmbh.copper.persistent;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Collection;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import de.scoopgmbh.copper.Response;
import de.scoopgmbh.copper.batcher.AbstractBatchCommand;
import de.scoopgmbh.copper.batcher.BatchExecutor;
import de.scoopgmbh.copper.batcher.NullCallback;
import de.scoopgmbh.copper.db.utility.RetryingTransaction;

class MySqlNotify {
	
	static final class Command extends AbstractBatchCommand<Executor, Command>{
		
		final Response<?> response;
		final DataSource dataSource;
		final Serializer serializer;

		@SuppressWarnings("unchecked")
		public Command(Response<?> response, DataSource dataSource, Serializer serializer) {
			super(NullCallback.instance,250);
			this.response = response;
			this.dataSource = dataSource;
			this.serializer = serializer;
		}

		@Override
		public Executor executor() {
			return Executor.INSTANCE;
		}

	}
	
	static final class Executor extends BatchExecutor<Executor, Command>{

		private static final Executor INSTANCE = new Executor();
		private static final Logger logger = Logger.getLogger(Executor.class);

		@Override
		public int maximumBatchSize() {
			return 100;
		}

		@Override
		public int preferredBatchSize() {
			return 50;
		}

		@Override
		protected void executeCommands(final Collection<Command> commands) {
			if (commands.isEmpty())
				return;
			
			try {
				new RetryingTransaction(commands.iterator().next().dataSource) {
					@Override
					protected void execute() throws Exception {
						final Timestamp now = new Timestamp(System.currentTimeMillis());
						final PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO COP_RESPONSE (CORRELATION_ID, RESPONSE_TS, RESPONSE) VALUES (?,?,?)");
						for (Command cmd : commands) {
							stmt.setString(1, cmd.response.getCorrelationId());
							stmt.setTimestamp(2, now);
							String payload = cmd.serializer.serializeResponse(cmd.response);
							stmt.setString(3, payload);
							stmt.addBatch();
						}
						stmt.executeBatch();
					}
				}.run();
			} 
			catch (Exception e) {
				logger.error("",e);
				throw new RuntimeException(e);
				// todo Einzelverarbeitung...
			}
			
		}

	}

}