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
package rabbit.ui.tests.pages;

import rabbit.data.access.IAccessor;
import rabbit.data.handler.DataHandler;
import rabbit.ui.Preferences;
import rabbit.ui.internal.pages.AbstractTableViewerPage;
import rabbit.ui.internal.pages.CommandPage;

import static org.junit.Assert.assertEquals;

import org.eclipse.core.commands.Command;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Map;

/**
 * Test for {@link CommandPage}
 */
public class CommandPageTest extends AbstractTableViewerPageTest {

  private static ICommandService getCommandService() {
    return (ICommandService) PlatformUI.getWorkbench().getService(
        ICommandService.class);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetValue() throws Exception {
    Field field = CommandPage.class.getDeclaredField("dataMapping");
    field.setAccessible(true);
    Map<Command, Long> data = (Map<Command, Long>) field.get(page);

    Command command = getCommandService().getDefinedCommands()[0];
    long value = 1989;
    data.put(command, value);
    assertEquals(value, page.getValue(command));

    Command noValueCommand = getCommandService().getCommand(
        System.currentTimeMillis() + "");
    assertEquals(0, page.getValue(noValueCommand));
  }

  @Test
  public void testUpdate() throws Exception {
    long max = 0;
    IAccessor<Map<String, Long>> accessor = DataHandler
        .getCommandDataAccessor();

    Preferences pref = new Preferences();
    Map<String, Long> data = accessor.getData(
        new LocalDate(pref.getStartDate()), new LocalDate(pref.getEndDate()));
    for (long value : data.values()) {
      if (value > max) {
        max = value;
      }
    }
    page.update(pref);
    assertEquals(max, page.getMaxValue());

    pref.getStartDate().add(Calendar.MONTH, -1);
    pref.getEndDate().add(Calendar.DAY_OF_MONTH, -5);
    data = accessor.getData(new LocalDate(pref.getStartDate()), new LocalDate(
        pref.getEndDate()));
    max = 0;
    for (long value : data.values()) {
      if (value > max) {
        max = value;
      }
    }
    page.update(pref);
    assertEquals(max, page.getMaxValue());
  }

  @Override
  protected AbstractTableViewerPage createPage() {
    return new CommandPage();
  }

}
