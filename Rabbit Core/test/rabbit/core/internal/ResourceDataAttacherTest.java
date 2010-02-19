package rabbit.core.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.reflect.Field;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.internal.Workbench;
import org.junit.Assert;
import org.junit.Test;

import rabbit.core.internal.storage.xml.XmlResourceManager;

@SuppressWarnings("restriction")
public class ResourceDataAttacherTest {

	@Test
	public void testWorkbenchListenerIsAttached() throws Exception {
		Field field = Workbench.class.getDeclaredField("workbenchListeners");
		field.setAccessible(true);
		ListenerList listeners = (ListenerList) field.get(Workbench.getInstance());
		for (Object listener : listeners.getListeners()) {
			if (listener == XmlResourceManager.INSTANCE) {
				return;
			}
		}
		Assert.fail();
	}

	@Test
	public void testResourceListenerIsAttached() throws Exception {
		// Simple test to test the listener is attached, does not test the
		// accuracy of the results.

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("tmp");
		if (!project.exists()) {
			project.create(null);
		}
		if (!project.isOpen()) {
			project.open(null);
		}

		IPath oldPath = project.getFullPath();
		XmlResourceManager.INSTANCE.insert(oldPath.toString());

		IPath newPath = Path.fromPortableString(project.getFullPath().toString() + System.currentTimeMillis());
		assertNull(XmlResourceManager.INSTANCE.getId(newPath.toString()));
		project.move(newPath, true, null);

		assertNull(XmlResourceManager.INSTANCE.getId(oldPath.toString()));
		assertNotNull(XmlResourceManager.INSTANCE.getId(newPath.toString()));
	}
}