# Script tu dong debloat va cai dat CleanYourTV cho Onn TV Box (Google TV)
# Cach dung:
#   powershell -ExecutionPolicy Bypass -File tools\setup-onn.ps1 -TvHost <IP_CUA_ONN_BOX>
# Hoac chi can chay:
#   powershell -ExecutionPolicy Bypass -File tools\setup-onn.ps1 (script se hoi IP)

param(
    [string]$TvHost = $env:TV_HOST
)

$ErrorActionPreference = 'Stop'
$OutputEncoding = New-Object System.Text.UTF8Encoding $false
$repo = Split-Path -Parent $PSScriptRoot

Write-Host "=====================================================" -ForegroundColor Cyan
Write-Host "   CLEANYOURTV - TOI UU & CAI DAT CHO ONN GOOGLE TV  " -ForegroundColor Cyan
Write-Host "=====================================================" -ForegroundColor Cyan

if (-not $TvHost) {
    $TvHost = Read-Host "Nhap dia chi IP cua Onn TV Box (vi du: 192.168.1.50)"
}
if (-not $TvHost) {
    Write-Error "Chua nhap dia chi IP cua TV Box. Dung tien trinh."
    exit 1
}

$env:TV_HOST = $TvHost
Write-Host "[1/4] Kiem tra moi truong Python..." -ForegroundColor Yellow
$py = Join-Path $repo 'tools\venv\Scripts\python.exe'
if (-not (Test-Path $py)) {
    Write-Host "Dang khoi tao moi truong ao python tools\venv..." -ForegroundColor Gray
    python -m venv (Join-Path $repo 'tools\venv')
    & $py -m pip install -r (Join-Path $repo 'tools\requirements.txt')
}

Write-Host "[2/4] Ket noi thu toi Onn Box tai $TvHost:5555..." -ForegroundColor Yellow
try {
    $probe = & $py -I (Join-Path $repo 'tools\tv.py') --probe
    Write-Host $probe -ForegroundColor Green
} catch {
    Write-Warning "Khong the ket noi toi $TvHost. Hay kiem tra xem ban da bat 'USB Debugging' tren Onn Box va TV co cung mang Wi-Fi hay chua."
    exit 1
}

Write-Host "[3/4] Ap dung danh sach debloat cho Onn 4K Google TV..." -ForegroundColor Yellow
$debloatFile = Join-Path $repo 'examples\onn-4k-streaming-box-2023\debloat.txt'
if (Test-Path $debloatFile) {
    powershell -ExecutionPolicy Bypass -File (Join-Path $repo 'tools\disable.ps1') -TvHost $TvHost $debloatFile
} else {
    Write-Warning "Khong tim thay file $debloatFile"
}

Write-Host "[4/4] Ap dung giao dien toi uu (Theme onn-vietnam)..." -ForegroundColor Yellow
$themeFolder = Join-Path $repo 'themes\onn-vietnam'
if (Test-Path $themeFolder) {
    powershell -ExecutionPolicy Bypass -File (Join-Path $repo 'tools\theme.ps1') -TvHost $TvHost $themeFolder
}

Write-Host ""
Write-Host "=====================================================" -ForegroundColor Green
Write-Host "   HOAN TAT! ONN BOX DA DUOC DEBLOAT & TOI UU!        " -ForegroundColor Green
Write-Host "=====================================================" -ForegroundColor Green
Write-Host "Neu ban can cai dat launcher moi, hay chay:" -ForegroundColor Gray
Write-Host "  powershell -ExecutionPolicy Bypass -File launcher\build-and-install.ps1 -TvHost $TvHost" -ForegroundColor White
Write-Host "Neu muon khoi phuc lai nhu cu, hay chay:" -ForegroundColor Gray
Write-Host "  powershell -ExecutionPolicy Bypass -File tools\restore.ps1 -TvHost $TvHost" -ForegroundColor White
