# Clean all Android Studio cache and force fresh project
Write-Host "Cleaning Android Studio cache..."

# Remove .idea folder
if (Test-Path ".idea") {
    Remove-Item -Path ".idea" -Recurse -Force
    Write-Host "Removed .idea folder"
}

# Remove .gradle folder
if (Test-Path ".gradle") {
    Remove-Item -Path ".gradle" -Recurse -Force
    Write-Host "Removed .gradle folder"
}

# Remove build folders
if (Test-Path "build") {
    Remove-Item -Path "build" -Recurse -Force
    Write-Host "Removed build folder"
}

if (Test-Path "app\build") {
    Remove-Item -Path "app\build" -Recurse -Force
    Write-Host "Removed app/build folder"
}

# Remove local.properties
if (Test-Path "local.properties") {
    Remove-Item -Path "local.properties" -Force
    Write-Host "Removed local.properties"
}

Write-Host "`n✅ Cache cleaned!"
Write-Host "`nNow:"
Write-Host "1. Close Android Studio completely"
Write-Host "2. Reopen Android Studio"
Write-Host "3. Open this project as a NEW project"
Write-Host "4. It will show as 'Einstein Ai'"

# Commit changes
git add .
git commit -m "Clean cache for fresh project"
git push

Write-Host "`nPushed to GitHub!"

# Self-delete
Remove-Item "clean_cache.ps1" -Force
