/**
 * Remove existing highlights
 */
$("input").removeClass('highlight');
$("select[id=topicCategory]").removeClass('highlight');
$("textarea").removeClass('highlight');
$("select").parent().filter('.highlightSelectWrapper').find('select').unwrap();

/**
 * Add new highlights
 */
$("input[id^=smlIdentifier]").addClass('highlight');
$("input[id^=smlIdentification][id$=Label]").addClass('highlight');
$("input[id^=smlIdentification][id$=Value]").addClass('highlight');
$("input[id^=smlClassification][id$=Label]").addClass('highlight');
$("input[id^=smlClassification][id$=Value]").addClass('highlight');
$("input[id^=smlCapabilityText][id$=CapabilitiesName]").addClass('highlight');
$("input[id^=smlCapabilityText][id$=CapabilityName]").addClass('highlight');
$("input[id^=smlCapabilityText][id$=Definition]").addClass('highlight');
$("input[id^=smlKeyword_]").addClass('highlight');




