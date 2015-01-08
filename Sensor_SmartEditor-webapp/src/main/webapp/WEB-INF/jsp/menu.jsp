<%--

    See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    con terra GmbH licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License. You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
	function changeHighlightedFields() {
		$.getScript("js/validation/" + $('#validatorId').val() + ".js").done(
				function(script, textStatus) {
					//console.log( "Highlighted fields defined by " + $('#validatorId').val());
				})
	}
	function uniqueID_or_Title_missing() {
		var resource = $("#resTitle").attr("name");
		if (resource.indexOf("smlUniqueID")!="-1") {
			$("#dialog-uniqueID-template-missing").dialog('open');
		} else {
			$("#dialog-title-template-missing").dialog('open');
		}

	}
	$(document)
			.ready(
					function() {
						var bool_value = $('#stickyErrorMessages').val() == "true" ? true
								: false
						$("#validatorIsSticky").attr('checked', bool_value);
						// set cookie to indicate active session
						$.cookie('tc_smartEditorSession', 'active');

						// check that validator id is set, if not pick the first one
						if ($('#validatorId').val() == null
								|| $('#validatorId').val() == '') {
							$("input:radio[name=validator]:first-child").attr(
									'checked', true);
							$('#validatorId')
									.val(
											$(
													"input:radio[name=validator]:first-child")
													.attr('value'));
							changeHighlightedFields($('#validatorId').val());
						}

						$("#publish").click(function() {
							$('#target').val(null);
							$('#updateMetadata').submit();
						});
						$("#saveLocal").click(
								function() {
									$('#target').val('saveLocal');
									// turn off validation
									var old = $('input[name=validatorId]')
											.attr("value");
									$('input[name=validatorId]').attr("value",
											"");
									$('#updateMetadata').submit();
									// reset value
									$('input[name=validatorId]').attr("value",
											old);
								});

						$("#saveTemplate")
								.click(
										function() {
											if ($('#resTitle').val() == '') {
												uniqueID_or_Title_missing();
											} else {
												$("#dialog-waiting").dialog(
														'open');
												$
														.throbberShow({
															image : "images/ajax-loader-small.gif",
															ajax : "true",
															parent : "#waiting"
														});
												$('#target')
														.val('saveTemplate');
												// turn off validation
												var old = $(
														'input[name=validatorId]')
														.attr("value");
												$('input[name=validatorId]')
														.attr("value", "");
												$
														.ajax({
															type : "POST",
															url : "${pageContext.request.contextPath}/edit.do",
															data : $(
																	'#updateMetadata')
																	.serialize(),
															success : function() {
																$
																		.throbberHide();
																$(
																		"#dialog-waiting")
																		.dialog(
																				'close');
																$(
																		"#dialog-template-ok")
																		.dialog(
																				'open');
															},
															error : function() {
																$
																		.throbberHide();
																$(
																		"#dialog-waiting")
																		.dialog(
																				'close');
																$(
																		"#dialog-template-error")
																		.dialog(
																				'open');
															},
															complete : function() {
																$(
																		'input[name=validatorId]')
																		.attr(
																				"value",
																				old);
															}
														});
											}
										});

						$("#saveDraft")
								.click(
										function() {
											if ($('#resTitle').val() == '') {
												uniqueID_or_Title_missing();
											} else {
												$("#dialog-waiting").dialog(
														'open');
												$
														.throbberShow({
															image : "images/ajax-loader-small.gif",
															ajax : "true",
															parent : "#waiting"
														});
												$('#target').val('saveDraft');
												// turn off validation
												var old = $(
														'input[name=validatorId]')
														.attr("value");
												$('input[name=validatorId]')
														.attr("value", "");
												$
														.ajax({
															type : "POST",
															url : "${pageContext.request.contextPath}/edit.do",
															data : $(
																	'#updateMetadata')
																	.serialize(),
															success : function() {
																$
																		.throbberHide();
																$(
																		"#dialog-waiting")
																		.dialog(
																				'close');
																$(
																		"#dialog-draft-ok")
																		.dialog(
																				'open');
															},
															error : function() {
																$
																		.throbberHide();
																$(
																		"#dialog-waiting")
																		.dialog(
																				'close');
																$(
																		"#dialog-draft-error")
																		.dialog(
																				'open');
															},
															complete : function() {
																$(
																		'input[name=validatorId]')
																		.attr(
																				"value",
																				old);
															}
														});

											}
										});

						$("#validationOptions").click(function() {
							$("#dialog-validation").dialog('open');
						});

						$("input[name='validator']").click(function() {
							$('#validatorId').val($(this).val());
							changeHighlightedFields();
						});

						$("#validatorIsSticky").click(
								function() {
									$('#stickyErrorMessages').val(
											$(this).is(':checked'));
								});

						$("#dialog-validation").bind(
								"dialogopen",
								function(event, ui) {
									// set correct radio boxes...
									$(
											"input[name='validator'][value='"
													+ $('#validatorId').val()
													+ "']").attr("checked",
											true);
								});

						$("#dialog-waiting").dialog({
							autoOpen : false,
							modal : true
						});

						$("#dialog-validation")
								.dialog(
										{
											autoOpen : false,
											width : 350,
											modal : false,
											buttons : {
												'<fmt:message key="editor.general.widget.ok"/>' : function() {
													$(this).dialog('close');
												}
											}
										});
						$("#dialog-draft-ok")
								.dialog(
										{
											autoOpen : false,
											modal : false,
											buttons : {
												'<fmt:message key="editor.general.widget.ok"/>' : function() {
													$(this).dialog('close');
												}
											}
										});
						$("#dialog-template-ok")
								.dialog(
										{
											autoOpen : false,
											modal : false,
											buttons : {
												'<fmt:message key="editor.general.widget.ok"/>' : function() {
													$(this).dialog('close');
												}
											}
										});
						$("#dialog-draft-error")
								.dialog(
										{
											autoOpen : false,
											modal : false,
											buttons : {
												'<fmt:message key="editor.general.widget.ok"/>' : function() {
													$(this).dialog('close');
												}
											}
										});
						$("#dialog-template-error")
								.dialog(
										{
											autoOpen : false,
											modal : false,
											buttons : {
												'<fmt:message key="editor.general.widget.ok"/>' : function() {
													$(this).dialog('close');
												}
											}
										});
						$("#dialog-title-draft-missing")
								.dialog(
										{
											autoOpen : false,
											modal : false,
											buttons : {
												'<fmt:message key="editor.general.widget.ok"/>' : function() {
													$(this).dialog('close');
												}
											}
										});
						$("#dialog-title-template-missing")
								.dialog(
										{
											autoOpen : false,
											modal : false,
											buttons : {
												'<fmt:message key="editor.general.widget.ok"/>' : function() {
													$(this).dialog('close');
												}
											}
										});
						$("#dialog-uniqueID-template-missing")
						.dialog(
								{
									autoOpen : false,
									modal : false,
									buttons : {
										'<fmt:message key="editor.general.widget.ok"/>' : function() {
											$(this).dialog('close');
										}
									}
								});
					});

</script>

<ul id="subMenu">
	<li><a class="publish" href="#" id="publish"> <fmt:message
				key="action.publish" />
	</a></li>
	<li><a class="save" href='#' id="saveLocal"> <fmt:message
				key="action.save.local" />
	</a></li>
	<li><a class="template" href='#' id="saveTemplate"> <fmt:message
				key="action.save.template" />
	</a></li>
	<li><a class="validation" href='#' id="validationOptions"> <fmt:message
				key="action.validation" />
	</a></li>
	<li><c:url value="/startEditor.do" var="startEditor" /> <a
		class="homepage" id="startEditor"
		href='<c:out value="${startEditor}"/>'> <fmt:message
				key="action.home" />
	</a></li>
	<li><c:url value="/exit.do" var="stopEditor" /> <a class="exit"
		id="stopEditor" href='<c:out value="${stopEditor}"/>'> <fmt:message
				key="action.stop" />
	</a></li>
	<li class="help" id="menuHelp"></li>
</ul>

<%-- ========================== --%>
<%-- Dialogs                    --%>
<%-- ========================== --%>
<div id="dialog-waiting" title="<fmt:message key="menu.waiting.title"/>">
	<p>
		<span id="waiting"></span>
		<fmt:message key="menu.waiting.message" />
	</p>
</div>

<div id="dialog-template-ok"
	title="<fmt:message key="menu.template.ok.title"/>">
	<p>
		<span class="ui-icon ui-icon-circle-check"
			style="float: left; margin: 0 7px 50px 0;"></span>
		<fmt:message key="menu.template.ok.message" />
	</p>
</div>

<div id="dialog-template-error"
	title="<fmt:message key="menu.template.error.title"/>">
	<p>
		<span class="ui-icon ui-icon-alert"
			style="float: left; margin: 0 7px 50px 0;"></span>
		<fmt:message key="menu.template.error.message" />
	</p>
</div>

<div id="dialog-draft-ok"
	title="<fmt:message key="menu.draft.ok.title"/>">
	<p>
		<span class="ui-icon ui-icon-circle-check"
			style="float: left; margin: 0 7px 50px 0;"></span>
		<fmt:message key="menu.draft.ok.message" />
	</p>
</div>

<div id="dialog-draft-error"
	title="<fmt:message key="menu.draft.error.title"/>">
	<p>
		<span class="ui-icon ui-icon-alert"
			style="float: left; margin: 0 7px 50px 0;"></span>
		<fmt:message key="menu.draft.error.message" />
	</p>
</div>

<div id="dialog-validation"
	title="<fmt:message key="element.validation.title"/>">
	<span> <fmt:message key="element.validation.message[0]" /><br>
		<p>&nbsp;</p> <fmt:message key="element.validation.message[1]" />
		<p>&nbsp;</p>
	</span>

	<p>
		<c:forEach items="${validators}" var="valIds">
			<span> <input name="validator" id="${valIds}"
				value="${valIds}" type="radio"> <label for="${valIds}"
				style="float: none; display: inline"> <fmt:message
						key="element.validation.${valIds}.label" /><br>
			</label> <fmt:message key="element.validation.${valIds}.message" /><br>
			</span>
			<p>&nbsp;</p>
		</c:forEach>
	<p>&nbsp;</p>
	<span> <input name="validatorIsSticky" id="validatorIsSticky"
		value="true" type="checkbox"> <label for="validatorIsSticky"
		style="float: none; display: inline; font-weight: 100"> <fmt:message
				key="element.validation.sticky.message" /><br>
	</label>

	</span>
	</p>
</div>

<div id="dialog-title-draft-missing"
	title="<fmt:message key="menu.template.title.missing.title"/>">
	<p>
		<span class="ui-icon ui-icon-alert"
			style="float: left; margin: 0 7px 50px 0;"></span>
		<fmt:message key="menu.template.title.missing.draft.message" />
	</p>
</div>

<div id="dialog-title-template-missing"
	title="<fmt:message key="menu.template.title.missing.title"/>">
	<p>
		<span class="ui-icon ui-icon-alert"
			style="float: left; margin: 0 7px 50px 0;"></span>
		<fmt:message key="menu.template.title.missing.template.message" />
	</p>
</div>
<div id="dialog-uniqueID-template-missing"
	title="<fmt:message key="menu.template.uniqueID.missing.uniqueID"/>">
	<p>
		<span class="ui-icon ui-icon-alert"
			style="float: left; margin: 0 7px 50px 0;"></span>
		<fmt:message key="menu.template.uniqueID.missing.template.message" />
	</p>
</div>