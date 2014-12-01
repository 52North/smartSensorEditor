/**
 * Remove existing highlights
 */

$("select").parent().filter('.highlightSelectWrapper').find('select').unwrap();

/**
 * Add new highlights
 */
$("input[id=resTitle]").addClass('highlight');
$("input[id^=smlShortName]").addClass('highlight');
$("input[id=smlLongName]").addClass('highlight');
$("input[id^=smlUniqueID_]").addClass('highlight');
$("input[id^=smlKeyword_0]").addClass('highlight');




