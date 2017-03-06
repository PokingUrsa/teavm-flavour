/*
 *  Copyright 2015 Alexey Andreev.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.teavm.flavour.widgets;

import java.util.function.Supplier;
import org.teavm.flavour.templates.BindAttribute;
import org.teavm.flavour.templates.BindContent;
import org.teavm.flavour.templates.BindDirective;
import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Fragment;
import org.teavm.flavour.templates.OptionalBinding;
import org.teavm.flavour.templates.Slot;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.events.MouseEvent;

@BindDirective(name = "action-link")
@BindTemplate("templates/flavour/widgets/action-link.html")
public class ActionLink extends AbstractWidget {
    private Supplier<Boolean> enabled;
    private Supplier<Boolean> visible;
    private EventListener<MouseEvent> clickListener;
    private Fragment content;

    public ActionLink(Slot slot) {
        super(slot);
    }

    public boolean isEnabled() {
        return enabled == null || enabled.get();
    }

    @BindAttribute(name = "enabled")
    @OptionalBinding
    public void setEnabled(Supplier<Boolean> enabled) {
        this.enabled = enabled;
    }

    public boolean isVisible() {
        return visible == null || visible.get();
    }

    @BindAttribute(name = "visible")
    @OptionalBinding
    public void setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
    }

    public EventListener<MouseEvent> getClickListener() {
        return clickListener;
    }

    @BindAttribute(name = "onclick")
    @OptionalBinding
    public void setClickListener(EventListener<MouseEvent> clickListener) {
        this.clickListener = clickListener;
    }

    public Fragment getContent() {
        return content;
    }

    @BindContent
    public void setContent(Fragment content) {
        this.content = content;
    }
}
