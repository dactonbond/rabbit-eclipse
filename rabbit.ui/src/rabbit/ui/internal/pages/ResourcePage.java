/*
 * Copyright 2010 The Rabbit Eclipse Plug-in Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rabbit.ui.internal.pages;

import rabbit.data.IFileMapper;
import rabbit.data.access.IAccessor;
import rabbit.data.handler.DataHandler;
import rabbit.ui.CellPainter;
import rabbit.ui.DisplayPreference;
import rabbit.ui.TreeLabelComparator;
import rabbit.ui.internal.SharedImages;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A page for displaying time spent working on different files.
 */
public class ResourcePage extends AbstractTreeViewerPage {

  public static enum ShowMode {
    FILE, FOLDER, PROJECT
  }

  private ShowMode mode = ShowMode.FILE;

  private IAccessor<Map<String, Long>> accessor;
  private IFileMapper resourceMapper;
  private Map<IProject, Set<IResource>> projectResources;
  private Map<IFolder, Set<IFile>> folderFiles;
  private Map<IFile, Long> fileValues;

  private IAction collapseAllAction = new Action("Collapse All") {
    @Override
    public void run() {
      getViewer().collapseAll();
    }
  };

  private IAction expandAllAction = new Action("Expand All") {
    @Override
    public void run() {
      getViewer().expandAll();
    }
  };

  private IAction showFilesAction = new Action("Show Files",
      IAction.AS_CHECK_BOX) {
    @Override
    public void run() {
      showFoldersAction.setChecked(false);
      showProjectsAction.setChecked(false);
      setShowMode(ShowMode.FILE);
    }
  };

  private IAction showFoldersAction = new Action("Show Folders",
      IAction.AS_CHECK_BOX) {
    @Override
    public void run() {
      showFilesAction.setChecked(false);
      showProjectsAction.setChecked(false);
      setShowMode(ShowMode.FOLDER);
    }
  };

  private IAction showProjectsAction = new Action("Show Projects",
      IAction.AS_CHECK_BOX) {
    @Override
    public void run() {
      showFoldersAction.setChecked(false);
      showFilesAction.setChecked(false);
      setShowMode(ShowMode.PROJECT);
    }
  };

  public ResourcePage() {
    super();
    accessor = DataHandler.getFileDataAccessor();
    resourceMapper = DataHandler.getFileMapper();

    projectResources = new HashMap<IProject, Set<IResource>>();
    folderFiles = new HashMap<IFolder, Set<IFile>>();
    fileValues = new HashMap<IFile, Long>();
  }

  @Override
  public void createContents(Composite parent) {
    super.createContents(parent);
    getViewer().addFilter(new ViewerFilter() {
      @Override
      public boolean select(Viewer viewer, Object parentElement, Object element) {
        switch (getShowMode()) {
        case PROJECT:
          return element instanceof IProject;
        case FOLDER:
          return element instanceof IContainer;
        default:
          return true;
        }
      }
    });
  }

  @Override
  public IContributionItem[] createToolBarItems(IToolBarManager toolBar) {
    expandAllAction.setImageDescriptor(SharedImages.EXPAND_ALL);
    IContributionItem expandAll = new ActionContributionItem(expandAllAction);
    toolBar.add(expandAll);

    ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
    ImageDescriptor img = images
        .getImageDescriptor(ISharedImages.IMG_ELCL_COLLAPSEALL);
    collapseAllAction.setImageDescriptor(img);
    IContributionItem collapseAll = new ActionContributionItem(
        collapseAllAction);
    toolBar.add(collapseAll);

    Separator sep = new Separator();
    toolBar.add(sep);

    img = images.getImageDescriptor(IDE.SharedImages.IMG_OBJ_PROJECT);
    showProjectsAction.setImageDescriptor(img);
    showProjectsAction.setChecked(getShowMode() == ShowMode.PROJECT);
    IContributionItem showProjects = new ActionContributionItem(
        showProjectsAction);
    toolBar.add(showProjects);

    img = images.getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
    showFoldersAction.setImageDescriptor(img);
    showFoldersAction.setChecked(getShowMode() == ShowMode.FOLDER);
    IContributionItem showFolders = new ActionContributionItem(
        showFoldersAction);
    toolBar.add(showFolders);

    img = images.getImageDescriptor(ISharedImages.IMG_OBJ_FILE);
    showFilesAction.setImageDescriptor(img);
    showFilesAction.setChecked(getShowMode() == ShowMode.FILE);
    IContributionItem showFiles = new ActionContributionItem(showFilesAction);
    toolBar.add(showFiles);

    return new IContributionItem[] { expandAll, collapseAll, sep, showProjects,
        showFolders, showFiles };
  }

  public IFile[] getFiles(IFolder folder) {
    Set<IFile> files = folderFiles.get(folder);
    if (files != null) {
      return files.toArray(new IFile[files.size()]);
    }
    return new IFile[0];
  }

  public long getMaxFileValue() {
    long max = 0;
    for (long value : fileValues.values()) {
      if (value > max) {
        max = value;
      }
    }
    return max;
  }

  public long getMaxFolderValue() {
    long max = 0;
    for (IFolder folder : folderFiles.keySet()) {
      long value = getValueOfFolder(folder);
      if (value > max) {
        max = value;
      }
    }
    return max;
  }

  public long getMaxProjectValue() {
    long max = 0;
    for (IProject project : projectResources.keySet()) {
      long value = getValueOfProject(project);
      if (value > max) {
        max = value;
      }
    }
    return max;
  }

  public IResource[] getResources(IProject project) {
    Set<IResource> resources = projectResources.get(project);
    if (resources != null) {
      return resources.toArray(new IResource[resources.size()]);
    }
    return new IResource[0];
  }

  public ShowMode getShowMode() {
    return mode;
  }

  @Override
  public long getValue(Object o) {
    if (o instanceof IFile) {
      return getValueOfFile((IFile) o);
    } else if (o instanceof IFolder) {
      return getValueOfFolder((IFolder) o);
    } else if (o instanceof IProject) {
      return getValueOfProject((IProject) o);
    } else {
      return 0;
    }
  }

  public long getValueOfFile(IFile file) {
    Long value = fileValues.get(file);
    return (null == value) ? 0 : value;
  }

  public long getValueOfFolder(IFolder folder) {
    Set<IFile> files = folderFiles.get(folder);
    if (files == null) {
      return 0;
    }
    long value = 0;
    for (IFile file : files) {
      value += getValueOfFile(file);
    }
    return value;
  }

  public long getValueOfProject(IProject project) {
    Set<IResource> resources = projectResources.get(project);
    if (resources == null) {
      return 0;
    }
    long value = 0;
    for (IResource resource : resources) {
      value += (resource instanceof IFile) ? getValueOfFile((IFile) resource)
          : getValueOfFolder((IFolder) resource);
    }
    return value;
  }

  public void setShowMode(ShowMode newMode) {
    if (mode == newMode) {
      return;
    }

    collapseAllAction.setEnabled(!(newMode == ShowMode.PROJECT));
    expandAllAction.setEnabled(collapseAllAction.isEnabled());

    mode = newMode;
    updateMaxValue();
    getViewer().refresh();
  }

  @Override
  public boolean shouldPaint(Object element) {
    return (element instanceof IProject && getShowMode() == ShowMode.PROJECT)
        || (element instanceof IFolder && getShowMode() == ShowMode.FOLDER)
        || (element instanceof IFile && getShowMode() == ShowMode.FILE);
  }

  @Override
  public void update(DisplayPreference p) {
    Object[] elements = getViewer().getExpandedElements();
    doUpdate(accessor.getData(p.getStartDate(), p.getEndDate()));
    try {
      getViewer().setExpandedElements(elements);
    } catch (IllegalArgumentException e) {
      // Do nothing.
    }
  }

  @Override
  protected CellLabelProvider createCellPainter() {
    return new CellPainter(this) {
      @Override
      protected Color createColor(Display display) {
        return new Color(display, 136, 177, 231);
      }
    };
  }

  @Override
  protected void createColumns(TreeViewer viewer) {
    TreeLabelComparator textSorter = new TreeLabelComparator(viewer);
    TreeLabelComparator valueSorter = createValueSorterForTree(viewer);

    int[] widths = new int[] { 200, 150 };
    int[] styles = new int[] { SWT.LEFT, SWT.RIGHT };
    String[] names = new String[] { "Name", "Time Spent" };

    for (int i = 0; i < names.length; i++) {
      TreeColumn column = new TreeColumn(viewer.getTree(), styles[i]);
      column.setText(names[i]);
      column.setWidth(widths[i]);
      column.addSelectionListener((names.length - 1 == i) ? valueSorter
          : textSorter);
    }
  }

  @Override
  protected TreeLabelComparator createComparator(TreeViewer viewer) {
    return new TreeLabelComparator(viewer) {

      @Override
      public int category(Object element) {
        if (element instanceof IProject) {
          return 1;
        } else if (element instanceof IFolder) {
          return 2;
        } else if (element instanceof IFile) {
          return 3;
        } else {
          return 0;
        }
      }
    };
  }

  @Override
  protected ITreeContentProvider createContentProvider() {
    return new ResourcePageContentProvider(this);
  }

  @Override
  protected ITableLabelProvider createLabelProvider() {
    return new ResourcePageDecoratingLabelProvider(this,
        new ResourcePageLabelProvider(), PlatformUI.getWorkbench()
            .getDecoratorManager().getLabelDecorator());
  };

  private void doUpdate(Map<String, Long> data) {
    setMaxValue(0);

    projectResources.clear();
    folderFiles.clear();
    fileValues.clear();

    for (Map.Entry<String, Long> entry : data.entrySet()) {
      IFile file = resourceMapper.getFile(entry.getKey());
      if (file == null) {
        file = resourceMapper.getExternalFile(entry.getKey());
      }
      if (file == null) {
        continue;
      }

      Long oldValue = fileValues.get(file);
      if (oldValue == null) {
        oldValue = Long.valueOf(0);
      }
      fileValues.put(file, entry.getValue() + oldValue);

      IProject project = file.getProject();
      IContainer folder = file.getParent();

      Set<IResource> resources = projectResources.get(project);
      if (resources == null) {
        resources = new HashSet<IResource>();
        projectResources.put(project, resources);
      }

      if (project == folder) {
        resources.add(file);
      } else {
        resources.add(folder);
        Set<IFile> fileset = folderFiles.get(folder);
        if (fileset == null) {
          fileset = new HashSet<IFile>();
          folderFiles.put((IFolder) folder, fileset);
        }
        fileset.add(file);
      }
    }
    updateMaxValue();
    getViewer().setInput(projectResources.keySet());
  };

  private void updateMaxValue() {
    switch (getShowMode()) {
    case FILE:
      setMaxValue(getMaxFileValue());
      break;
    case FOLDER:
      setMaxValue(getMaxFolderValue());
      break;
    default:
      setMaxValue(getMaxProjectValue());
      break;
    }
  }
}