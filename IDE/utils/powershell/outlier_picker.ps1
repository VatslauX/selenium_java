
param(
  [string]$browser = 'firefox',
  [switch]$grid,
  [switch]$full
)

function removeFrequentKey {
  param(
    [object]$frequencies
  )
  $max_freq = $frequencies.Values | Sort-Object -Descending | Select-Object -First 1
  # Collection was modified; enumeration operation may not execute..
  # $frequencies.Keys | foreach-object { if ( $frequencies.Item($_) -eq $max_freq ) {$frequencies.Remove($_)}}

  $result = @{}
  $frequencies.Keys | ForEach-Object { if ($frequencies.Item($_) -ne $max_freq) { $result[$_] = $frequencies.Item($_) } }
  return $result
}

$MODULE_NAME = 'selenium_utils.psd1'
Import-Module -Name ('{0}/{1}' -f '.',$MODULE_NAME)

# $selenium = launch_selenium -browser $browser
if ([bool]$PSBoundParameters['grid'].IsPresent) {
  $selenium = launch_selenium -browser $browser -grid

} else {
  $selenium = launch_selenium -browser $browser

}

[OpenQA.Selenium.Interactions.Actions]$actions = New-Object OpenQA.Selenium.Interactions.Actions ($selenium)

$base_url = "file:///C:/developer/sergueik/powershell_selenium/powershell/data.html"
$selenium.Navigate().GoToUrl($base_url)
$selenium.Navigate().Refresh()

$modules = @{}

# module tables locator
$table_css_selector = 'html body div table.sortable'

# rows locator (relative to table)
$row_css_selector = 'tbody tr'

# columns locators (relative to row) - the td:nth-child(...) is used
# puppet master server
$server_column_number = 1
# module
$module_column_number = 2
# git hash
$githash_column_number = 3

# wait for the page to load
try {
  [OpenQA.Selenium.Support.UI.WebDriverWait]$wait = New-Object OpenQA.Selenium.Support.UI.WebDriverWait ($selenium,[System.TimeSpan]::FromSeconds(30))
  $wait.PollingInterval = 25
  [void]$wait.Until([OpenQA.Selenium.Support.UI.ExpectedConditions]::ElementIsVisible([OpenQA.Selenium.By]::CssSelector($row_css_selector)))
} catch [exception]{
  Write-Output ("Exception : {0} ...`n(ignored)" -f (($_.Exception.Message) -split "`n")[0])
}

$hash_column_css_selector = ('td:nth-child({0})' -f $githash_column_number)

$hashesFinderScript = @"

	// var table_selector = 'html body div table.sortable';
	// var row_selector = 'tbody tr';
	// var column_selector = 'td:nth-child(3)';

	var table_selector = '${table_css_selector}';
	var row_selector = '${row_css_selector}';
	var column_selector = '${hash_column_css_selector}';
	col_num = 0;
	var tables = document.querySelectorAll(table_selector);
	var git_hashes = {};
	for (table_cnt = 0; table_cnt != tables.length; table_cnt++) {
		var table = tables[table_cnt];
		if (table instanceof Element) {
			var rows = table.querySelectorAll(row_selector);
			// skip first row
			for (row_cnt = 1; row_cnt != rows.length; row_cnt++) {
				var row = rows[row_cnt];
				if (row instanceof Element) {
					var cols = row.querySelectorAll(column_selector);
					if (cols.length > 0) {
						data = cols[0].innerHTML;
						data = data.replace(/\s+/g, '');
						if (!git_hashes[data]) {
							git_hashes[data] = 0;
						}
						git_hashes[data]++;
					}
				}
			}
		}
	}
	var sortNumber = function(a, b) {
	// reverse numeric sort
		return b - a;
	}
	var removeFrequentKey = function(datahash) {
		var array_keys = [];
		var array_values = [];
		for (var key in datahash) {
			array_keys.push(key);
			array_values.push(0 + datahash[key]);
		}
		max_freq = array_values.sort(sortNumber)[0]
		for (var key in datahash) {
			if (datahash[key] === max_freq) {
				delete datahash[key]
			}
		}
		return datahash;
	}
	
	git_hashes = removeFrequentKey(git_hashes);
	
	var array_keys = [];
	for (var key in git_hashes) {
		array_keys.push(key);
	}
	
	return array_keys.join();
"@

$result = ([OpenQA.Selenium.IJavaScriptExecutor]$selenium).executeScript($hashesFinderScript)
Write-Output ("Outliers: git hashes:`r`n{0}" -f $result)

$hash_column_css_selector = ('td:nth-child({0})' -f $githash_column_number)
$master_server_column_css_selector = ('td:nth-child({0})' -f $server_column_number)

[string]$serverFinderScript = @"

var table_selector = '${table_css_selector}';
var row_selector = '${row_css_selector}';
var hash_column_selector = '${hash_column_css_selector}';
var master_server_column_selector = '${master_server_column_css_selector}';
var git_hashes_str = arguments[0];

// var table_selector = 'html body div table.sortable';
// var row_selector = 'tbody tr';
// var hash_column_selector = 'td:nth-child(3)';
// var master_server_column_selector = 'td:nth-child(1)';
// var git_hashes_str = '259c762,25bad25,2bad762,b26e5f1,bade5f1,d1bad8d,d158d8d,533acf2,533ace2,1b24bca,1b24bc2,d3c1652,d3aaa52,7538e12,7000e12';

var result = {};
var col_num = 0;
var git_hashes = {};
var git_hashes_keys = git_hashes_str.split(',');
for (var key in git_hashes_keys) {
	git_hashes[git_hashes_keys[key]] = 1;
}
var tables = document.querySelectorAll(table_selector);


for (table_cnt = 0; table_cnt != tables.length; table_cnt++) {
	var table = tables[table_cnt];
	if (table instanceof Element) {
		var rows = table.querySelectorAll(row_selector);
		// skip first row
		for (row_cnt = 1; row_cnt != rows.length; row_cnt++) {
			var row = rows[row_cnt];
			if (row instanceof Element) {
				var hash_cols = row.querySelectorAll(hash_column_selector);
				if (hash_cols.length > 0) {
					data = hash_cols[0].innerHTML;
					data = data.replace(/\s+/g, '');
					if (git_hashes[data]) {
						var master_server_cols = row.querySelectorAll(master_server_column_selector);
						if (master_server_cols.length > 0) {
							data = master_server_cols[0].innerHTML;
							data = data.replace(/\s+/g, '');
							if (!result[data]) {
								result[data] = 0;
							}
							result[data]++;
						}
					}
				}
			}
		}
	}
}
var array_keys = [];
for (var key in result) {
	array_keys.push(key);
}
// TODO: collect 'module' column
return JSON.stringify(result);
return array_keys.join();

"@

$result = ([OpenQA.Selenium.IJavaScriptExecutor]$selenium).executeScript($serverFinderScript,([OpenQA.Selenium.IJavaScriptExecutor]$selenium).executeScript($hashesFinderScript ))


$result_obj = convertFrom-JSON $result 
$result_hash = @{}
$result_obj.psobject.properties | Foreach-object { $result_hash[$_.Name] = $_.Value }
Write-Output ("Outliers: master servers:`r`n{0}" -f ($result_hash.keys -join "`r`n"))

# cannot cast instance of type "System.Management.Automation.PSCustomObject" to type "System.Collections.Hashtable"
# custom code to convert simple PSCustomObject back to a hashtable
# TODO: add Newtonsoft.Json to sharedassemblies and JObject.Parse(json);
# https://github.com/JamesNK/Newtonsoft.Json
# $config = [Newtonsoft.Json.Linq.JObject]::Parse($result)
# $config.GetEnumerator()

$result_array = @()
[Newtonsoft.Json.Linq.JObject]::Parse($result) | foreach-object { $result_array   += $_.Path  } 
Write-Output ("Outliers: master servers:`r`n{0}" -f ($result_array -join "`r`n"))

if (-not ([bool]$PSBoundParameters['full'].IsPresent)) {
  # Cleanup
  cleanup ([ref]$selenium)
  exit
}

# iterate over modules
foreach ($table in ($selenium.FindElements([OpenQA.Selenium.By]::CssSelector($table_css_selector)))) {

  $max_rows = 100
  $row_cnt = 0
  $hashes = @{}
  $module = $null

  # iterate overs Puppet master server r10k hashes
  foreach ($row in ($table.FindElements([OpenQA.Selenium.By]::CssSelector($row_css_selector)))) {
    if ($row_cnt -eq 0) {
      # skil first row (table headers)
      $row_cnt++
      continue
    }
    if ($row_cnt -gt $max_rows) { break }
    $githash = $row.FindElement([OpenQA.Selenium.By]::CssSelector(('td:nth-child({0})' -f $githash_column_number))).Text
    if (-not $hashes[$githash]) {
      $hashes[$githash] = 1
      $module = $row.FindElement([OpenQA.Selenium.By]::CssSelector(('td:nth-child({0})' -f $module_column_number))).Text
      if (-not $modules[$module]) {
        $modules[$module] = $true
      }
    } else {
      $hashes[$githash]++
    }
    $row_cnt++
  }
  # Workaround Powershell flexible types
  $keys = @()
  $hashes.Keys | ForEach-Object { $keys += $_ }
  if ($keys.Length -gt 1) {
    Write-Output ('Module: {0}' -f $module)
    # Write-Output ('Hashes found: {0}' -f ($hashes.Keys -join "`r`n"))
    $hashes_amended = removeFrequentKey ($hashes)
    $first_row = $true
    foreach ($row2 in ($table.FindElements([OpenQA.Selenium.By]::CssSelector($row_css_selector)))) {
      if ($first_row) {
        # first row is table headers
        $first_row = $false
        continue
      }
      [OpenQA.Selenium.IWebElement]$githash_column = $row2.FindElement([OpenQA.Selenium.By]::CssSelector(('td:nth-child({0})' -f $githash_column_number)))
      if ($hashes_amended[$githash_column.Text]) {
        [void]$actions.MoveToElement([OpenQA.Selenium.IWebElement]$githash_column).Build().Perform()
        highlight -selenium_ref ([ref]$selenium) -element_ref ([ref]$githash_column) -color 'red'
        [OpenQA.Selenium.IWebElement]$server_column = $row2.FindElement([OpenQA.Selenium.By]::CssSelector(('td:nth-child({0})' -f $server_column_number)))
        highlight -selenium_ref ([ref]$selenium) -element_ref ([ref]$server_column) -color 'blue'
        Write-Output ('Master Server: ' + $server_column.Text)
      }
    }
  }
}
# Cleanup
cleanup ([ref]$selenium)

