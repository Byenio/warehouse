import { env } from "process";
import ManufacturerForm from "./manufacturerForm";

export default async function FormPage() {
  const data = await fetch(
    `http://${env.SERVER_ADDRESS}:${env.SERVER_PORT}/api/country`,
    { cache: "force-cache" }
  );
  const countryData: CountryInfo[] = await data.json();

  return (
    <div>
      <ManufacturerForm formData={countryData} />
    </div>
  );
}
