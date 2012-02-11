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

package rabbit.tracking.internal.workbench;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.joda.time.Duration;
import org.joda.time.Instant;

import rabbit.tracking.TimedEvent;
import rabbit.tracking.workbench.ILaunchEvent;

import com.google.common.collect.ImmutableSet;

final class LaunchEvent extends TimedEvent implements ILaunchEvent {

  private final ILaunch launch;
  private final ILaunchConfiguration config;
  private final ILaunchConfigurationType type;
  private final Set<IPath> filePaths;

  /*
   * Note that ILaunch.getLaunchConfiguration() and
   * ILaunchConfiguration.getType() returns the objects we want but may return
   * null, (we don't want null) therefore we specified them as non null
   * parameters rather than just taking the ILaunch.
   */

  LaunchEvent(
      Instant instant,
      Duration duration,
      ILaunch launch,
      ILaunchConfiguration config,
      ILaunchConfigurationType type,
      Set<IPath> filePaths) {
    super(instant, duration);
    this.type = checkNotNull(type, "type");
    this.config = checkNotNull(config, "config");
    this.launch = checkNotNull(launch, "launch");
    this.filePaths = ImmutableSet.copyOf(checkNotNull(filePaths, "filePaths"));
  }

  @Override public final Set<IPath> getFilePaths() {
    return filePaths;
  }

  @Override public final ILaunch getLaunch() {
    return launch;
  }

  @Override public final ILaunchConfigurationType getLaunchConfigurationType() {
    return type;
  }

  @Override public final ILaunchConfiguration getLaunchConfiguration() {
    return config;
  }

  @Override public String toString() {
    return toStringHelper()
        .add("launch", getLaunch())
        .add("launchConfiguration", getLaunchConfiguration())
        .add("launchConfigurationType", getLaunchConfigurationType())
        .add("filePaths", getFilePaths())
        .toString();
  }
}
