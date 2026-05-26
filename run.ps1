Get-Content .env | ForEach-Object {
    if ($_ -match '^(.*?)=(.*)$') {
        $name = $Matches[1].Trim()
        $value = $Matches[2].Trim()
        [System.Environment]::SetEnvironmentVariable($name, $value)
    }
}
.\mvnw.cmd spring-boot:run
