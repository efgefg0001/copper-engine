/*
 * Copyright 2002-2013 SCOOP Software GmbH
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
package de.scoopgmbh.copper.gui.ui.repository.result;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import de.scoopgmbh.copper.gui.adapter.GuiCopperDataProvider;
import de.scoopgmbh.copper.gui.context.FormContext;
import de.scoopgmbh.copper.gui.form.FxmlController;
import de.scoopgmbh.copper.gui.form.filter.FilterResultController;
import de.scoopgmbh.copper.gui.ui.repository.filter.WorkflowRepositoryFilterModel;
import de.scoopgmbh.copper.gui.ui.workflowclasssesctree.WorkflowClassesTreeController;
import de.scoopgmbh.copper.gui.ui.workflowclasssesctree.WorkflowClassesTreeController.DisplayWorkflowClassesModel;
import de.scoopgmbh.copper.gui.util.WorkflowVersion;

public class WorkflowRepositoryResultController implements Initializable, FilterResultController<WorkflowRepositoryFilterModel,WorkflowVersion>, FxmlController {
	
	private final GuiCopperDataProvider copperDataProvider;
	private final FormContext formContext;
	private WorkflowClassesTreeController workflowClassesTreeController;
	
	public WorkflowRepositoryResultController(GuiCopperDataProvider copperDataProvider, FormContext formContext) {
		super();
		this.copperDataProvider = copperDataProvider;
		this.formContext = formContext;
	}


    @FXML //  fx:id="search"
    private TextField search; // Value injected by FXMLLoader

    @FXML //  fx:id="workflowView"
    private TreeView<DisplayWorkflowClassesModel> workflowView; // Value injected by FXMLLoader


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert search != null : "fx:id=\"search\" was not injected: check your FXML file 'WorkflowRepositoryResult.fxml'.";
        assert workflowView != null : "fx:id=\"workflowView\" was not injected: check your FXML file 'WorkflowRepositoryResult.fxml'.";

        workflowClassesTreeController = formContext.createWorkflowClassesTreeController(workflowView);
        
        search.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue!=null){
					TreeItem<DisplayWorkflowClassesModel> item = search(workflowView.getRoot(),newValue);
					if (item!=null){
						workflowView.getSelectionModel().select(item);	
					} else {
						workflowView.getSelectionModel().clearSelection();
					}
				}
				if (newValue==null){
					workflowView.getSelectionModel().clearSelection();
				}
			}
		});
    }
    
    private TreeItem<DisplayWorkflowClassesModel> search(TreeItem<DisplayWorkflowClassesModel> item, String regex){
    	Pattern p = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);
    	for (TreeItem<DisplayWorkflowClassesModel> child: item.getChildren()){
    		if (child.getValue().displayname!=null && p.matcher(child.getValue().displayname).matches()){
    			return child;
    		} else {
    			return search(child,regex);
    		}
    	}
		return null;
    }
    
	@Override
	public URL getFxmlRessource() {
		return getClass().getResource("WorkflowRepositoryResult.fxml");
	}

	@Override
	public void showFilteredResult(List<WorkflowVersion> filteredResult, WorkflowRepositoryFilterModel usedFilter) {
		workflowClassesTreeController.refresh(filteredResult);
	}

	@Override
	public List<WorkflowVersion> applyFilterInBackgroundThread(WorkflowRepositoryFilterModel filter) {
		return copperDataProvider.getWorkflowClassesList(filter.enginePoolModel.selectedEngine.get().getId());  
	}
	
	@Override
	public boolean canLimitResult() {
		return false;
	}
	
	@Override
	public void clear() {
		if (workflowView.getRoot()!=null){
			workflowView.getRoot().getChildren().clear();
		}
	}

}