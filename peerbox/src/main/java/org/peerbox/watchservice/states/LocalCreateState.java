package org.peerbox.watchservice.states;

import java.io.File;
import java.nio.file.Path;

import org.hive2hive.core.exceptions.IllegalFileLocation;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.peerbox.FileManager;
import org.peerbox.watchservice.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * the create state handles all events which would like 
 * to alter the state from "create" to another state (or keep the current state) and decides
 * whether an transition into another state is allowed. 
 * 
 * 
 * @author winzenried
 *
 */

public class LocalCreateState extends AbstractActionState {
	private final static Logger logger = LoggerFactory.getLogger(LocalCreateState.class);
	
	public LocalCreateState(Action action) {
		super(action);
	}
	
	/**
	 * State is already set to Create, therefore this event does not change the current state
	 * 
	 * @return a new CreateState object
	 */
	@Override
	public AbstractActionState handleLocalCreateEvent() {
		logger.debug("Create Request denied: Already in Create State.");
		//return new CreateState();
		throw new IllegalStateException("Create Request denied: Already in Create State.");
	}

	/**
	 * Delete events will be accepted, however the state will change to Initial
	 * since any previous create action will not be excuted and therefore there is
	 * nothing to delete
	 * 
	 * @return new InitialState object
	 */
	@Override
	public AbstractActionState handleLocalDeleteEvent() {
		logger.debug("Delete Request accepted: State changed from Create to Initial.");
		return new InitialState(action);
	}

	/**
	 * The transition from Create to Modify is valid, but since any preceding create action
	 * was not yet executed, a modified file will be considered as "new" and therefore handled
	 * as a create event
	 * 
	 * @return new creatState object
	 */
	@Override
	public AbstractActionState handleLocalModifyEvent() {
		logger.debug("Modify Request accepted: State remains Create.");
		return this;
	}
	
	@Override
	public AbstractActionState handleLocalMoveEvent(Path oldFilePath) {
		return new LocalMoveState(action, oldFilePath);
		//throw new RuntimeException("Not implemented...");
	}

	@Override
	public AbstractActionState handleRemoteCreateEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractActionState handleRemoteDeleteEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractActionState handleRemoteModifyEvent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractActionState handleRemoteMoveEvent(Path oldFilePath) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * If the create state is considered as stable, the execute method will be invoked which eventually
	 * uploads the file with the corresponding Hive2Hive method
	 * 
	 * @param file The file which should be uploaded
	 */
	@Override
	public void execute(FileManager fileManager) throws NoSessionException, NoPeerConnectionException, IllegalFileLocation {
		Path filePath = action.getFilePath();
		File file = filePath.toFile();
		fileManager.add(file);
		
		logger.debug("Task \"Add File\" executed.");
	}
}
