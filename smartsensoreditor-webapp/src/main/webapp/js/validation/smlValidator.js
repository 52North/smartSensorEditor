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
$("input[id^=smlShortName]").addClass('highlight');
$("input[id=smlLongName]").addClass('highlight');
$("input[id^=resTitle]").addClass('highlight');
$("input[id^=smlKeyword_0]").addClass('highlight');




