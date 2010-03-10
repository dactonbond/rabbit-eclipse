package rabbit.core.internal.storage.xml;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.IPath;

import rabbit.core.RabbitCore;
import rabbit.core.internal.storage.xml.schema.events.EventListType;
import rabbit.core.internal.storage.xml.schema.events.ObjectFactory;

/**
 * Data stores.
 */
public enum DataStore implements IDataStore {

	COMMAND_STORE("commandEvents"),
	PART_STORE("partEvents"),
	PERSPECTIVE_STORE("perspectiveEvents"),
	FILE_STORE("fileEvents");

	/**
	 * Formats a date into "yyyy-MM".
	 */
	private final DateFormat monthFormatter = new SimpleDateFormat("yyyy-MM");

	/**
	 * An object factory for creating XML object types.
	 */
	private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory();

	private String id;

	private DataStore(String id) {
		this.id = id;
	}

	@Override
	public File getDataFile(Calendar date) {
		return getDataFile(date, getStorageLocation());
	}

	@Override
	public File getDataFile(Calendar date, IPath location) {
		return location.append(id + "-" + monthFormatter.format(date.getTime()))
				.addFileExtension("xml").toFile();
	}

	@Override
	public List<File> getDataFiles(Calendar startDate, Calendar endDate) {
		Calendar start = (Calendar) startDate.clone();
		start.set(Calendar.DAY_OF_MONTH, 1);

		Calendar end = (Calendar) endDate.clone();
		end.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DAY_OF_MONTH));

		List<File> result = new ArrayList<File>();
		IPath[] storagePaths = RabbitCore.getDefault().getStoragePaths();
		while (start.compareTo(end) <= 0) {

			for (IPath path : storagePaths) {
				File f = getDataFile(start, path);
				if (f.exists())
					result.add(f);
			}

			start.add(Calendar.MONTH, 1);
		}
		return result;
	}

	@Override
	public EventListType read(File file) {
		try {
			if (file.exists()) {
				return JaxbUtil.unmarshal(EventListType.class, file);
			} else {
				return OBJECT_FACTORY.createEventListType();
			}

		} catch (JAXBException e) {
			return OBJECT_FACTORY.createEventListType();
		}
	}

	@Override
	public void write(EventListType doc, File f) {
		try {
			JaxbUtil.marshal(OBJECT_FACTORY.createEvents(doc), f);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IPath getStorageLocation() {
		IPath path = RabbitCore.getDefault().getStoragePath();
		File f = path.toFile();
		if (!f.exists()) {
			if (!f.mkdirs()) {
				System.err.println(getClass() + ": Cannot create storage location.");
			}
		}
		return path;
	}

}
