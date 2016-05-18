package com.elenore.csclonechecker;

import java.util.HashMap;
import java.util.Map;

public class ScratchScriptDictionary {

	private Map<String, Integer> map;
	ScratchScriptDictionary() {
		this.map = new HashMap<String, Integer>();
		this.initDictionary();
	}

	private void initDictionary() {

		//Events
		this.map.put("whenGreenFlag",0);
		this.map.put("whenKeyPressed", 0);
		this.map.put("whenClicked", 0);
		this.map.put("whenSceneStarts", 0);
		this.map.put("whenSensorGreaterThan", 0);
		this.map.put("whenIReceive", 0);

		//variable, lists
		this.map.put("setVar:to:", 0);
		this.map.put("changeVar:by:", 0);
		this.map.put("readVariable", 0);
		this.map.put("getParam", 0);
		this.map.put("append:toList:", 0);
		this.map.put("call", 0);
		this.map.put("getLine:ofList:",0);

		//pen
		this.map.put("clearPenTrails", 0);
		this.map.put("stampCostume", 0);
		this.map.put("putPenDown", 0);
		this.map.put("putPenUp", 0);
		this.map.put("penColor:", 0);
		this.map.put("changePenSizeBy:", 0);
		this.map.put("penSize:", 0);

		//move
		this.map.put("forward:", 0);
		this.map.put("turnRight:", 0);
		this.map.put("turnLeft:", 0);
		this.map.put("heading:", 0);
		this.map.put("pointTowards:", 0);
		this.map.put("gotoX:y", 0);
		this.map.put("gotoSpriteOrMouse:", 0);
		this.map.put("glideSecs:toX:y:elapsed:from:", 0);
		this.map.put("changeXposBy:", 0);
		this.map.put("xpos:", 0);
		this.map.put("changeYposBy:", 0);
		this.map.put("ypos:", 0);
		this.map.put("bounceOffEdge", 0);
		this.map.put("setRotationStyle", 0);

		//sound
		this.map.put("broadcast:", 0);
		this.map.put("playSound:", 0);
		this.map.put("doPlaySoundAndWait", 0);
		this.map.put("stopAllSounds", 0);

		//looks
		this.map.put("say:duration:elapsed:from:", 0);
		this.map.put("say:", 0);
		this.map.put("think:duration:elapsed:from:", 0);
		this.map.put("think:", 0);
		this.map.put("show", 0);
		this.map.put("hide", 0);
		this.map.put("lookLike:", 0);
		this.map.put("nextCostume", 0);
		this.map.put("startScene", 0);
		this.map.put("changeGraphicEffect:by:", 0);
		this.map.put("setGraphicEffect:to:", 0);
		this.map.put("changeSizeBy:", 0);
		this.map.put("setSizeTo:", 0);
		this.map.put("comeToFront", 0);
		this.map.put("goBackByLayers", 0);

		//loop
		this.map.put("wait:elapsed:from:", 0);
		this.map.put("doRepeat", 0);
		this.map.put("doIf", 0);
		this.map.put("doIfElse", 0);
		this.map.put("doWaitUntil", 0);
		this.map.put("doUntil", 0);
		this.map.put("timerReset", 0);
		this.map.put("randomFrom:to:",0);
		this.map.put("deleteClone",0);

	}
	
	

}
