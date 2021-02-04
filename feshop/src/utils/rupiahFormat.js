export const rupiahFormat = (numb) => {
	if (numb){
		const format = numb.toString().split('').reverse().join('')
		const convert = format.match(/\d{1,3}/g)
		return `Rp.${convert.join('.').split('').reverse().join('')}`
	} 
	return numb
}
