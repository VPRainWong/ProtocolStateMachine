package com.vp.plugin.sample.statediagram2.actions;

import java.awt.Point;

import com.vp.plugin.ApplicationManager;
import com.vp.plugin.DiagramManager;
import com.vp.plugin.action.VPAction;
import com.vp.plugin.action.VPActionController;
import com.vp.plugin.diagram.IDiagramTypeConstants;
import com.vp.plugin.diagram.IStateDiagramUIModel;
import com.vp.plugin.diagram.connector.ITransition2UIModel;
import com.vp.plugin.diagram.shape.IInitialPseudoStateUIModel;
import com.vp.plugin.diagram.shape.IState2UIModel;
import com.vp.plugin.model.ICallTrigger;
import com.vp.plugin.model.IClass;
import com.vp.plugin.model.IConstraintElement;
import com.vp.plugin.model.IInitialPseudoState;
import com.vp.plugin.model.IOperation;
import com.vp.plugin.model.IState2;
import com.vp.plugin.model.ITransition2;
import com.vp.plugin.model.factory.IModelElementFactory;

public class StateDiagram2ActionControl implements VPActionController {

	@Override
	public void performAction(VPAction arg0) {
		
		// create blank state diagram
		DiagramManager diagramManager = ApplicationManager.instance().getDiagramManager();
		IStateDiagramUIModel diagram = (IStateDiagramUIModel) diagramManager.createDiagram(IDiagramTypeConstants.DIAGRAM_TYPE_STATE_DIAGRAM);
		// name the diagram as "door"
		diagram.setName("door");
		// specify the diagram as Protocol State Machine
		diagram.setProtocolStateMachine(true);

		// create "Door" class as the classifier of the protocol state machine
		IClass doorClassModel = IModelElementFactory.instance().createClass();
		doorClassModel.setName("Door");
		
		// define operations for the Door class
		IOperation opsCreate = doorClassModel.createOperation();
		opsCreate.setName("create");
		opsCreate.setVisibility(IOperation.VISIBILITY_PRIVATE);
		
		IOperation opsOpen = doorClassModel.createOperation();
		opsOpen.setName("open");
		opsOpen.setVisibility(IOperation.VISIBILITY_PRIVATE);
		
		IOperation opsClose = doorClassModel.createOperation();
		opsClose.setName("close");
		opsClose.setVisibility(IOperation.VISIBILITY_PRIVATE);
		
		IOperation opsLock = doorClassModel.createOperation();
		opsLock.setName("lock");
		opsLock.setVisibility(IOperation.VISIBILITY_PRIVATE);
		
		IOperation opsUnlock = doorClassModel.createOperation();
		opsUnlock.setName("unlock");
		opsUnlock.setVisibility(IOperation.VISIBILITY_PRIVATE);
		
		// specify the protocol state machine as sub-diagram of Door class
		doorClassModel.addSubDiagram(diagram);
		
		// create initial pseudo state model
		IInitialPseudoState initialState1Model = IModelElementFactory.instance().createInitialPseudoState();
		// create shape for initial pseudo state
		IInitialPseudoStateUIModel initialState1Shape = (IInitialPseudoStateUIModel) diagramManager.createDiagramElement(diagram, initialState1Model);		
		// specify its location and size
		initialState1Shape.setBounds(22, 81, 20, 20);
		
		// create model for "opened" state
		IState2 stateOpenedModel = IModelElementFactory.instance().createState2();		
		stateOpenedModel.setName("opened");
		// create shape for "opened" state
		IState2UIModel stateOpenedShape = (IState2UIModel) diagramManager.createDiagramElement(diagram, stateOpenedModel);
		// specify its location and size
		stateOpenedShape.setBounds(135, 71, 80, 40);
		// set to automatic calculate the initial caption position		
		stateOpenedShape.setRequestResetCaption(true);
		
		IState2 stateClosedModel = IModelElementFactory.instance().createState2();		
		stateClosedModel.setName("closed");
		IState2UIModel stateClosedShape = (IState2UIModel) diagramManager.createDiagramElement(diagram, stateClosedModel);
		stateClosedShape.setBounds(356, 71, 80, 40);
		stateClosedShape.setRequestResetCaption(true);
		
		IState2 stateLockedModel = IModelElementFactory.instance().createState2();		
		stateLockedModel.setName("locked");
		IState2UIModel stateLockedShape = (IState2UIModel) diagramManager.createDiagramElement(diagram, stateLockedModel);
		stateLockedShape.setBounds(356, 191, 80, 40);
		stateLockedShape.setRequestResetCaption(true);

		
		// create transition model between initial state and opened state
		ITransition2 tranInitialOpenedModel = IModelElementFactory.instance().createTransition2();
		// specify the from end as the initial state, and the to end is opened state 
		tranInitialOpenedModel.setFrom(initialState1Model);
		tranInitialOpenedModel.setTo(stateOpenedModel);
		// create connector into diagram
		ITransition2UIModel tranInitialOpenedShape = (ITransition2UIModel) diagramManager.createConnector(diagram, tranInitialOpenedModel, initialState1Shape, stateOpenedShape, null);
		tranInitialOpenedShape.setRequestResetCaption(true);

		// create call trigger
		ICallTrigger callTriggerCreateModel = IModelElementFactory.instance().createCallTrigger();
		callTriggerCreateModel.setName("create");
		// specify the operation for the call trigger
		callTriggerCreateModel.setOperation(opsLock);
		// add call trigger to transition
		tranInitialOpenedModel.addTrigger(callTriggerCreateModel);		
				
		ITransition2 tranOpenedClosedModel = IModelElementFactory.instance().createTransition2();
		tranOpenedClosedModel.setFrom(stateOpenedModel);
		tranOpenedClosedModel.setTo(stateClosedModel);
		ITransition2UIModel tranOpenedClosedShape = (ITransition2UIModel) diagramManager.createConnector(diagram, tranOpenedClosedModel, stateOpenedShape, stateClosedShape, new Point[] {new Point(215, 91), new Point(356, 91)});
		tranOpenedClosedShape.setRequestResetCaption(true);

		ICallTrigger callTriggerCloseModel = IModelElementFactory.instance().createCallTrigger();
		callTriggerCloseModel.setName("close");
		callTriggerCloseModel.setOperation(opsClose);
		tranOpenedClosedModel.addTrigger(callTriggerCloseModel);
		
		// create constraint element as the guard condition
		IConstraintElement guard = IModelElementFactory.instance().createConstraintElement();
		// specify the detail of the guard condition
		guard.setName("doorway->isEmpty()");
		// set the guard condition into transition
		tranOpenedClosedModel.setGuard(guard);

		
		ITransition2 tranClosedLockedModel = IModelElementFactory.instance().createTransition2();
		tranClosedLockedModel.setFrom(stateClosedModel);
		tranClosedLockedModel.setTo(stateLockedModel);
		ITransition2UIModel tranClosedLockedShape = (ITransition2UIModel) diagramManager.createConnector(diagram, tranClosedLockedModel, stateClosedShape, stateLockedShape, new Point[] {new Point(396, 111), new Point(396, 191)});
		tranClosedLockedShape.setRequestResetCaption(true);

		ICallTrigger callTriggerLockModel = IModelElementFactory.instance().createCallTrigger();
		callTriggerLockModel.setName("lock");
		callTriggerLockModel.setOperation(opsLock);
		tranClosedLockedModel.addTrigger(callTriggerLockModel);					
		
		ITransition2 tranLockedClosedModel = IModelElementFactory.instance().createTransition2();
		tranLockedClosedModel.setFrom(stateLockedModel);
		tranLockedClosedModel.setTo(stateClosedModel);
		ITransition2UIModel tranLockedClosedShape = (ITransition2UIModel) diagramManager.createConnector(diagram, tranLockedClosedModel, stateLockedShape, stateClosedShape, new Point[] {new Point(436, 211), new Point(480, 211), new Point(480, 91), new Point(436, 91)});
		tranLockedClosedShape.setRequestResetCaption(true);

		ICallTrigger callTriggerUnlockModel = IModelElementFactory.instance().createCallTrigger();
		callTriggerUnlockModel.setName("unlock");
		callTriggerUnlockModel.setOperation(opsUnlock);
		tranLockedClosedModel.addTrigger(callTriggerUnlockModel);					
		
		ITransition2 tranClosedOpenedModel = IModelElementFactory.instance().createTransition2();
		tranClosedOpenedModel.setFrom(stateClosedModel);
		tranClosedOpenedModel.setTo(stateOpenedModel);
		ITransition2UIModel tranClosedOpenedShape = (ITransition2UIModel) diagramManager.createConnector(diagram, tranClosedOpenedModel, stateClosedShape, stateOpenedShape, new Point[] {new Point(396, 71), new Point(396, 41), new Point(175, 41), new Point(175, 71)});
		tranClosedOpenedShape.setRequestResetCaption(true);

		ICallTrigger callTriggerOpenModel = IModelElementFactory.instance().createCallTrigger();
		callTriggerOpenModel.setName("open");
		callTriggerOpenModel.setOperation(opsOpen);
		tranClosedOpenedModel.addTrigger(callTriggerOpenModel);					
	
		// show up the diagram		
		diagramManager.openDiagram(diagram);				
	
	}

	@Override
	public void update(VPAction arg0) {
		// TODO Auto-generated method stub
		
	}

}
