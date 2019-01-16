package party.lemons.statues.gui;

import net.minecraft.client.gui.GuiTextField;

public class GuiEdit extends GuiElement {
	GuiTextField field;
	String tempString="";

	public GuiEdit(int x, int y, int w, int h) {
		super(x, y, w, h);
	}

	@Override
	public void onAdded() {
		field = new GuiTextField(0, gui.fontRendererObj(), x, y, w, h);
		setText(tempString);
	}

	public void onChange()
	{

	}

	public String getText() {
		if (field == null) {
			return tempString;
		} else {
			return field.getText();
		}
	}

	public void setText(String text) {
		if (field == null) {
			tempString = text;
		} else {
			field.setText(text);
		}

		onChange();
	}

	@Override
	public void render() {
		field.drawTextBox();
	}

	@Override
	public void mouseDown(InputMouseEvent ev) {
		field.mouseClicked(ev.x, ev.y, ev.button);

		if (isMouseOver(ev)) {
			ev.handled = true;
		}
	}

	@Override
	public void keyPressed(InputKeyboardEvent ev) {
		field.textboxKeyTyped(ev.character, ev.key);

		if (field.isFocused()) {
			ev.handled = true;
		}
		onChange();

	}
}
