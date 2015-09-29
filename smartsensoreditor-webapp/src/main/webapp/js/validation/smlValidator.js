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
$("input[id^=smlKeyword_0]").addClass('highlight');




