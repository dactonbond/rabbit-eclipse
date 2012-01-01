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
package rabbit.ui.internal.actions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.junit.Test;

import rabbit.ui.internal.util.ICategory;
import rabbit.ui.internal.util.IVisualProvider;

public class PaintCategoryActionTest {

  @Test public void runShouldSetTheProvidersPaintCategory() {
    @SuppressWarnings("serial")
    class MyException extends RuntimeException {
    }
    ICategory category = mock(ICategory.class);
    IVisualProvider provider = mock(IVisualProvider.class);
    doThrow(new MyException()).when(provider).setVisualCategory(category);

    IAction action = create(category, provider);
    try {
      action.run();
      fail();
    } catch (MyException e) {
      // Pass
    }
  }

  @Test public void shouldSetTheImageOfTheActionToBeTheSameAsTheImageOfTheCategory() {
    ICategory category = mock(ICategory.class);
    ImageDescriptor image = mock(ImageDescriptor.class);
    given(category.getImageDescriptor()).willReturn(image);

    IAction action = create(category);
    assertThat(action.getImageDescriptor(),
        equalTo(category.getImageDescriptor()));
  }

  @Test public void shouldSetTheTextOfTheActionToBeTheSameAsTheTextOfTheCategory() {
    ICategory category = mock(ICategory.class);
    given(category.getText()).willReturn("Hello");

    IAction action = create(category);
    assertThat(action.getText(), equalTo(category.getText()));
  }

  @Test(expected = NullPointerException.class)//
  public void shouldThrowAnExceptionIfTryToConstructWithoutACategory() {
    create(null, mock(IVisualProvider.class));
  }

  @Test(expected = NullPointerException.class)//
  public void shouldThrowAnExceptionIfTryToConstructWithANullProvider() {
    create(mock(ICategory.class), new IVisualProvider[]{null});
  }

  /**
   * @see PaintCategoryAction#PaintCategoryAction(ICategory, IVisualProvider...)
   */
  protected PaintCategoryAction create(ICategory paintCategory,
      IVisualProvider... providers) {
    return new PaintCategoryAction(paintCategory, providers);
  }
}
