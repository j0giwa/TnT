function changeActive() {
  const currentPath = window.location.href

  const navlinks= document.getElementsByClassName('navlink')
  for(let i = 0; i < navlinks.length; i++) {
    const href = navlinks[i].href
    if(currentPath.includes(href))
      navlinks[i].classList.add('active')
    else
      navlinks[i].classList.remove('active')
  }
}
changeActive()