<?php

$locals=['en','ru','uk','be','ka','az','hy','kk','uz','de',
				'fr','it','es','pt','pl','bg','sr','el','hu','da',
				'is','lv','lt','et','nb','nl','ro','sq','sk','sl',
				'fi','tr','hr','cs','sv','ms','in','id','iw','he',
				'ca','af','ar','am','th','hi','bn','ja','ko','vi',
				'mk','bs','mn'];
	
$outFn='locales.js';
	
$cnt='var locales={'."\n";
	
$localsCount=0;	
foreach($locals as $loc)
{
	$lcnt=file_get_contents("lc/".$loc.".json");
	
	if($lcnt) {	
	
		$lca=json_decode($lcnt,true);
		if($lca) {
			$localsCount++;
			
			$cnt.="\t".'"'.$loc.'": {'."\n";
			$cnt.="\t\t".'"font": '.$lca['font'].','."\n";
			$cnt.="\t\t".'"fontSize": 64,'."\n";
			
			
			if(in_array($loc,array('ru','uk','be'))) {
				$cnt.="\t\t".'"t1": "Стрелять",'."\n";
				$cnt.="\t\t".'"t2": "Взрывать",'."\n";
				$cnt.="\t\t".'"t3": "Уничтожать",'."\n";
				$cnt.="\t\t".'"t4": "Побеждать!",'."\n";
			}
			else {
				$cnt.="\t\t".'"t1": "Shoot \'em all",'."\n";
				$cnt.="\t\t".'"t2": "Explode them",'."\n";
				$cnt.="\t\t".'"t3": "Destroy \'em up",'."\n";
				$cnt.="\t\t".'"t4": "Enjoy winning!",'."\n";
			}
			
			$cnt.="\t\t".'"tw": "'.$lca['youWin'].'",'."\n";
			$cnt.="\t\t".'"wFontSize": '.$lca['youWinSize'].','."\n";
			$cnt.="\t".'},'."\n";
			
			
		}
	}
}

$cnt.='}';

file_put_contents($outFn,$cnt);

echo $localsCount;

?>